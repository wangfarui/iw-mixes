package com.itwray.iw.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.auth.dao.AuthFamilyGroupDao;
import com.itwray.iw.auth.dao.AuthFamilyInviteDao;
import com.itwray.iw.auth.dao.AuthFamilyMemberDao;
import com.itwray.iw.auth.dao.AuthUserDao;
import com.itwray.iw.auth.mapper.AuthFamilyGroupMapper;
import com.itwray.iw.auth.model.dto.*;
import com.itwray.iw.auth.model.entity.AuthFamilyGroupEntity;
import com.itwray.iw.auth.model.entity.AuthFamilyInviteEntity;
import com.itwray.iw.auth.model.entity.AuthFamilyMemberEntity;
import com.itwray.iw.auth.model.entity.AuthUserEntity;
import com.itwray.iw.auth.model.enums.FamilyInviteStatusEnum;
import com.itwray.iw.auth.model.enums.FamilyMemberRoleEnum;
import com.itwray.iw.auth.model.enums.FamilyMemberStatusEnum;
import com.itwray.iw.auth.model.mq.FamilyGroupMemberLeaveMqDto;
import com.itwray.iw.auth.model.vo.FamilyGroupDetailVo;
import com.itwray.iw.auth.model.vo.FamilyInviteVo;
import com.itwray.iw.auth.model.vo.FamilyMemberVo;
import com.itwray.iw.auth.model.vo.FamilySharedQueryPolicyVo;
import com.itwray.iw.auth.model.vo.FamilySharedSavePolicyVo;
import com.itwray.iw.auth.service.AuthFamilyGroupService;
import com.itwray.iw.auth.utils.FamilyGroupUtils;
import com.itwray.iw.common.constants.BoolEnum;
import com.itwray.iw.common.utils.ConstantEnumUtil;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.web.exception.BusinessException;
import com.itwray.iw.web.model.enums.mq.FamilyGroupTopicEnum;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 家庭组 服务实现类
 *
 * @author wray
 * @since 2024-03-10
 */
@Service
@Slf4j
public class AuthFamilyGroupServiceImpl extends WebServiceImpl<AuthFamilyGroupDao, AuthFamilyGroupMapper, AuthFamilyGroupEntity,
        FamilyGroupAddDto, FamilyGroupUpdateDto, FamilyGroupDetailVo, Integer> implements AuthFamilyGroupService {

    private final AuthFamilyMemberDao familyMemberDao;
    private final AuthFamilyInviteDao familyInviteDao;
    private final AuthUserDao authUserDao;

    @Autowired
    public AuthFamilyGroupServiceImpl(AuthFamilyGroupDao baseDao,
                                      AuthFamilyMemberDao familyMemberDao,
                                      AuthFamilyInviteDao familyInviteDao,
                                      AuthUserDao authUserDao) {
        super(baseDao);
        this.familyMemberDao = familyMemberDao;
        this.familyInviteDao = familyInviteDao;
        this.authUserDao = authUserDao;
    }

    @Override
    @Transactional
    public Integer add(FamilyGroupAddDto dto) {
        Integer userId = UserUtils.getUserId();
        ensureNoActiveGroup(userId);

        // 创建家庭组
        AuthFamilyGroupEntity groupEntity = BeanUtil.copyProperties(dto, AuthFamilyGroupEntity.class);
        groupEntity.setOwnerUserId(userId);
        groupEntity.setStatus(BoolEnum.TRUE.getCode());
        getBaseDao().save(groupEntity);

        // 自动加入成员表（群主）
        AuthFamilyMemberEntity memberEntity = new AuthFamilyMemberEntity();
        memberEntity.setGroupId(groupEntity.getId());
        memberEntity.setUserId(userId);
        memberEntity.setRole(FamilyMemberRoleEnum.OWNER);
        memberEntity.setDefaultShared(BoolEnum.FALSE.getCode());
        memberEntity.setQueryOnlyMyself(BoolEnum.FALSE.getCode());
        memberEntity.setStatus(FamilyMemberStatusEnum.NORMAL);
        memberEntity.setJoinTime(LocalDateTime.now());
        familyMemberDao.save(memberEntity);

        // 自动切换到该家庭组
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, userId)
                .set(AuthUserEntity::getFamilyGroupId, groupEntity.getId())
                .update();

        return groupEntity.getId();
    }

    @Override
    @Transactional
    public void update(FamilyGroupUpdateDto dto) {
        // 校验权限（仅群主可修改）
        checkOwnerPermission(dto.getId());
        super.update(dto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 校验权限（仅群主可解散）
        checkOwnerPermission(id);

        // 查询所有成员
        List<AuthFamilyMemberEntity> memberList = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, id)
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .list();

        // 逻辑删除家庭组
        super.delete(id);

        // 更新所有成员状态为"已移除"
        if (!memberList.isEmpty()) {
            familyMemberDao.lambdaUpdate()
                    .eq(AuthFamilyMemberEntity::getGroupId, id)
                    .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                    .set(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.REMOVED)
                    .update();

            // 同步重置所有成员的 family_group_id 为 0
            List<Integer> userIds = memberList.stream()
                    .map(AuthFamilyMemberEntity::getUserId)
                    .collect(Collectors.toList());
            authUserDao.lambdaUpdate()
                    .in(AuthUserEntity::getId, userIds)
                    .eq(AuthUserEntity::getFamilyGroupId, id)
                    .set(AuthUserEntity::getFamilyGroupId, 0)
                    .update();

            // 发送离组消息（解散家庭组）
            this.sendFamilyMemberLeaveMessage(id, userIds);
        }

        // 使所有未使用的邀请码失效
        familyInviteDao.lambdaUpdate()
                .eq(AuthFamilyInviteEntity::getGroupId, id)
                .eq(AuthFamilyInviteEntity::getStatus, FamilyInviteStatusEnum.PENDING)
                .set(AuthFamilyInviteEntity::getStatus, FamilyInviteStatusEnum.EXPIRED)
                .update();
    }

    @Override
    @Transactional
    public FamilyInviteVo generateInvite(FamilyInviteGenerateDto dto) {
        Integer userId = UserUtils.getUserId();

        // 校验权限（群主、家长可生成邀请码）
        checkRolePermission(dto.getGroupId(), FamilyMemberRoleEnum.OWNER, FamilyMemberRoleEnum.PARENT);

        // 生成唯一邀请码（最多重试10次）
        String inviteCode = null;
        for (int i = 0; i < 10; i++) {
            String code = FamilyGroupUtils.generateInviteCode();
            // 校验邀请码是否唯一（只检查待使用状态的邀请码）
            Long count = familyInviteDao.lambdaQuery()
                    .eq(AuthFamilyInviteEntity::getInviteCode, code)
                    .eq(AuthFamilyInviteEntity::getStatus, FamilyInviteStatusEnum.PENDING)
                    .count();
            if (count == 0) {
                inviteCode = code;
                break;
            }
        }

        if (inviteCode == null) {
            throw new BusinessException("生成邀请码失败，请重试");
        }

        // 保存邀请码
        AuthFamilyInviteEntity inviteEntity = new AuthFamilyInviteEntity();
        inviteEntity.setGroupId(dto.getGroupId());
        inviteEntity.setInviteCode(inviteCode);
        inviteEntity.setInviterUserId(userId);
        inviteEntity.setValidHours(dto.getValidHours());
        inviteEntity.setExpireTime(LocalDateTime.now().plusHours(dto.getValidHours()));
        inviteEntity.setStatus(FamilyInviteStatusEnum.PENDING);
        familyInviteDao.save(inviteEntity);

        // 构建返回结果
        FamilyInviteVo vo = BeanUtil.copyProperties(inviteEntity, FamilyInviteVo.class);
        AuthUserEntity inviter = authUserDao.getById(userId);
        vo.setInviterName(inviter.getName());

        AuthFamilyGroupEntity group = getBaseDao().getById(dto.getGroupId());
        vo.setGroupName(group.getGroupName());

        return vo;
    }

    @Override
    public FamilyInviteVo validateInvite(String inviteCode) {
        // 校验邀请码格式
        if (!FamilyGroupUtils.isValidInviteCodeFormat(inviteCode)) {
            throw new BusinessException("邀请码已失效");
        }

        // 查询邀请码（只查询待使用状态的邀请码）
        AuthFamilyInviteEntity inviteEntity = familyInviteDao.lambdaQuery()
                .eq(AuthFamilyInviteEntity::getInviteCode, inviteCode)
                .eq(AuthFamilyInviteEntity::getStatus, FamilyInviteStatusEnum.PENDING)
                .one();

        if (inviteEntity == null) {
            throw new BusinessException("邀请码已失效");
        }

        // 校验是否过期
        if (LocalDateTime.now().isAfter(inviteEntity.getExpireTime())) {
            // 更新状态为已过期
            familyInviteDao.lambdaUpdate()
                    .eq(AuthFamilyInviteEntity::getId, inviteEntity.getId())
                    .set(AuthFamilyInviteEntity::getStatus, FamilyInviteStatusEnum.EXPIRED)
                    .update();
            throw new BusinessException("邀请码已失效");
        }

        // 查询家庭组信息
        AuthFamilyGroupEntity group = getBaseDao().getById(inviteEntity.getGroupId());
        if (group == null) {
            throw new BusinessException("家庭组不存在");
        }

        // 查询邀请人信息
        AuthUserEntity inviter = authUserDao.getById(inviteEntity.getInviterUserId());

        // 构建返回结果
        FamilyInviteVo vo = BeanUtil.copyProperties(inviteEntity, FamilyInviteVo.class);
        vo.setGroupName(group.getGroupName());
        vo.setInviterName(inviter != null ? inviter.getName() : "未知");

        return vo;
    }

    @Override
    public List<FamilyInviteVo> inviteList(Integer groupId) {
        // 校验权限（群主、家长可查看）
        checkRolePermission(groupId, FamilyMemberRoleEnum.OWNER, FamilyMemberRoleEnum.PARENT);

        // 查询邀请码列表
        List<AuthFamilyInviteEntity> inviteList = familyInviteDao.lambdaQuery()
                .eq(AuthFamilyInviteEntity::getGroupId, groupId)
                .orderByDesc(AuthFamilyInviteEntity::getCreateTime)
                .list();

        if (inviteList.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询邀请人信息
        List<Integer> inviterIds = inviteList.stream()
                .map(AuthFamilyInviteEntity::getInviterUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, String> inviterNameMap = authUserDao.lambdaQuery()
                .in(AuthUserEntity::getId, inviterIds)
                .list()
                .stream()
                .collect(Collectors.toMap(AuthUserEntity::getId, AuthUserEntity::getName));

        // 查询家庭组信息
        AuthFamilyGroupEntity group = getBaseDao().getById(groupId);

        // 构建返回结果
        return inviteList.stream().map(invite -> {
            FamilyInviteVo vo = BeanUtil.copyProperties(invite, FamilyInviteVo.class);
            vo.setGroupName(group.getGroupName());
            vo.setInviterName(inviterNameMap.getOrDefault(invite.getInviterUserId(), "未知"));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void join(FamilyGroupJoinDto dto) {
        Integer userId = UserUtils.getUserId();

        // 验证邀请码
        FamilyInviteVo inviteVo = validateInvite(dto.getInviteCode());

        // 查询用户当前是否已在其他家庭组
        AuthFamilyMemberEntity currentMember = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getUserId, userId)
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .one();

        // 已有家庭组时，必须先手动退出
        if (currentMember != null) {
            if (currentMember.getGroupId().equals(inviteVo.getGroupId())) {
                throw new BusinessException("您已是该家庭组成员");
            }
            throw new BusinessException("您已加入其他家庭组，请先退出当前家庭组");
        }

        // 校验人数上限
        AuthFamilyGroupEntity group = getBaseDao().getById(inviteVo.getGroupId());
        Long memberCount = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, inviteVo.getGroupId())
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .count();
        if (memberCount >= group.getMaxMember()) {
            throw new BusinessException("家庭组人数已达上限");
        }

        // 加入家庭组
        AuthFamilyMemberEntity memberEntity = new AuthFamilyMemberEntity();
        memberEntity.setGroupId(inviteVo.getGroupId());
        memberEntity.setUserId(userId);
        memberEntity.setRole(FamilyMemberRoleEnum.MEMBER);
        memberEntity.setDefaultShared(BoolEnum.FALSE.getCode());
        memberEntity.setQueryOnlyMyself(BoolEnum.FALSE.getCode());
        memberEntity.setStatus(FamilyMemberStatusEnum.NORMAL);
        memberEntity.setJoinTime(LocalDateTime.now());
        familyMemberDao.save(memberEntity);

        // 更新邀请码状态为已使用
        familyInviteDao.lambdaUpdate()
                .eq(AuthFamilyInviteEntity::getInviteCode, dto.getInviteCode())
                .set(AuthFamilyInviteEntity::getStatus, FamilyInviteStatusEnum.ACCEPTED)
                .update();

        // 自动切换到该家庭组
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, userId)
                .set(AuthUserEntity::getFamilyGroupId, inviteVo.getGroupId())
                .update();
    }

    @Override
    @Transactional
    public void quit(Integer groupId) {
        Integer userId = UserUtils.getUserId();

        // 查询成员信息
        AuthFamilyMemberEntity memberEntity = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, groupId)
                .eq(AuthFamilyMemberEntity::getUserId, userId)
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .one();

        if (memberEntity == null) {
            throw new BusinessException("您不是该家庭组成员");
        }

        // 群主不能退出
        if (FamilyMemberRoleEnum.OWNER.equals(memberEntity.getRole())) {
            throw new BusinessException("群主不能退出，请先解散家庭组");
        }

        // 更新成员状态为已退出
        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getId, memberEntity.getId())
                .set(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.QUIT)
                .update();

        // 如果当前在该家庭组，切换到个人模式
        AuthUserEntity user = authUserDao.getById(userId);
        if (groupId.equals(user.getFamilyGroupId())) {
            authUserDao.lambdaUpdate()
                    .eq(AuthUserEntity::getId, userId)
                    .set(AuthUserEntity::getFamilyGroupId, 0)
                    .update();
        }

        this.sendFamilyMemberLeaveMessage(groupId, Collections.singletonList(userId));
    }

    @Override
    @Transactional
    public void removeMember(FamilyMemberRemoveDto dto) {
        Integer userId = UserUtils.getUserId();

        // 校验权限（仅群主可移除成员）
        checkOwnerPermission(dto.getGroupId());

        // 不能移除自己
        if (userId.equals(dto.getUserId())) {
            throw new BusinessException("不能移除自己");
        }

        // 查询成员信息
        AuthFamilyMemberEntity memberEntity = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, dto.getGroupId())
                .eq(AuthFamilyMemberEntity::getUserId, dto.getUserId())
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .one();

        if (memberEntity == null) {
            throw new BusinessException("该用户不是家庭组成员");
        }

        // 更新成员状态为已移除
        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getId, memberEntity.getId())
                .set(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.REMOVED)
                .update();

        // 如果被移除用户当前在该家庭组，切换到个人模式
        authUserDao.lambdaUpdate()
                .eq(AuthUserEntity::getId, dto.getUserId())
                .eq(AuthUserEntity::getFamilyGroupId, dto.getGroupId())
                .set(AuthUserEntity::getFamilyGroupId, 0)
                .update();

        this.sendFamilyMemberLeaveMessage(dto.getGroupId(), Collections.singletonList(dto.getUserId()));
    }

    @Override
    public List<FamilyMemberVo> memberList(Integer groupId) {
        // 校验权限（仅成员可查看）
        checkMemberPermission(groupId);

        // 查询成员列表
        List<AuthFamilyMemberEntity> memberList = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, groupId)
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .orderByAsc(AuthFamilyMemberEntity::getRole)
                .orderByAsc(AuthFamilyMemberEntity::getJoinTime)
                .list();

        if (memberList.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询用户信息
        List<Integer> userIds = memberList.stream()
                .map(AuthFamilyMemberEntity::getUserId)
                .collect(Collectors.toList());
        Map<Integer, AuthUserEntity> userMap = authUserDao.lambdaQuery()
                .in(AuthUserEntity::getId, userIds)
                .list()
                .stream()
                .collect(Collectors.toMap(AuthUserEntity::getId, user -> user));

        // 构建返回结果
        return memberList.stream().map(member -> {
            FamilyMemberVo vo = BeanUtil.copyProperties(member, FamilyMemberVo.class);
            AuthUserEntity user = userMap.get(member.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setName(user.getName());
                vo.setAvatar(user.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public FamilyGroupDetailVo myGroup() {
        Integer userId = UserUtils.getUserId();
        AuthUserEntity user = authUserDao.getById(userId);

        // 个人模式
        if (user.getFamilyGroupId() == null || user.getFamilyGroupId() == 0) {
            return null;
        }

        // 查询家庭组信息
        AuthFamilyGroupEntity group = getBaseDao().getById(user.getFamilyGroupId());
        if (group == null) {
            return null;
        }

        FamilyGroupDetailVo vo = BeanUtil.copyProperties(group, FamilyGroupDetailVo.class);
        AuthFamilyMemberEntity memberEntity = queryNormalMember(group.getId(), userId);
        if (memberEntity != null) {
            vo.setCurrentUserRole(memberEntity.getRole());
            vo.setDefaultShared(this.resolveDefaultShared(memberEntity));
            vo.setQueryOnlyMyself(this.resolveQueryOnlyMyself(memberEntity));
        }
        return vo;
    }

    @Override
    @Transactional
    public void transferOwner(FamilyGroupTransferDto dto) {
        Integer userId = UserUtils.getUserId();

        // 校验权限（仅群主可转让）
        checkOwnerPermission(dto.getGroupId());

        // 不能转让给自己
        if (userId.equals(dto.getNewOwnerUserId())) {
            throw new BusinessException("不能转让给自己");
        }

        // 查询新群主是否是家庭组成员
        AuthFamilyMemberEntity newOwnerMember = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, dto.getGroupId())
                .eq(AuthFamilyMemberEntity::getUserId, dto.getNewOwnerUserId())
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .one();

        if (newOwnerMember == null) {
            throw new BusinessException("该用户不是家庭组成员");
        }

        // 更新家庭组的群主
        getBaseDao().lambdaUpdate()
                .eq(AuthFamilyGroupEntity::getId, dto.getGroupId())
                .set(AuthFamilyGroupEntity::getOwnerUserId, dto.getNewOwnerUserId())
                .update();

        // 更新原群主的角色为普通成员
        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getGroupId, dto.getGroupId())
                .eq(AuthFamilyMemberEntity::getUserId, userId)
                .set(AuthFamilyMemberEntity::getRole, FamilyMemberRoleEnum.MEMBER)
                .update();

        // 更新新群主的角色为群主
        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getId, newOwnerMember.getId())
                .set(AuthFamilyMemberEntity::getRole, FamilyMemberRoleEnum.OWNER)
                .update();
    }

    @Override
    @Transactional
    public void assignRole(FamilyMemberRoleAssignDto dto) {
        Integer currentUserId = UserUtils.getUserId();
        FamilyMemberRoleEnum targetRole = ConstantEnumUtil.findByType(FamilyMemberRoleEnum.class, dto.getRole());
        if (targetRole == null || FamilyMemberRoleEnum.OWNER.equals(targetRole)) {
            throw new BusinessException("仅支持分配成员、家长、儿童角色");
        }

        // 操作人必须是群成员
        AuthFamilyMemberEntity operator = queryNormalMember(dto.getGroupId(), currentUserId);
        if (operator == null) {
            throw new BusinessException("您不是该家庭组成员");
        }

        if (currentUserId.equals(dto.getUserId())) {
            throw new BusinessException("不能修改自己的角色");
        }

        AuthFamilyMemberEntity targetMember = queryNormalMember(dto.getGroupId(), dto.getUserId());
        if (targetMember == null) {
            throw new BusinessException("该用户不是家庭组成员");
        }
        if (FamilyMemberRoleEnum.OWNER.equals(targetMember.getRole())) {
            throw new BusinessException("不能修改群主角色");
        }

        FamilyMemberRoleEnum operatorRole = operator.getRole();
        if (FamilyMemberRoleEnum.OWNER.equals(operatorRole)) {
            // 群主可将成员调整为家长/成员/儿童
        } else if (FamilyMemberRoleEnum.PARENT.equals(operatorRole)) {
            if (!FamilyMemberRoleEnum.MEMBER.equals(targetMember.getRole())
                    && !FamilyMemberRoleEnum.CHILD.equals(targetMember.getRole())) {
                throw new BusinessException("家长仅可调整成员或儿童角色");
            }
            if (!FamilyMemberRoleEnum.MEMBER.equals(targetRole)
                    && !FamilyMemberRoleEnum.CHILD.equals(targetRole)) {
                throw new BusinessException("家长仅可分配成员或儿童角色");
            }
        } else {
            throw new BusinessException("仅群主或家长可执行此操作");
        }

        FamilyMemberRoleEnum originRole = targetMember.getRole();
        boolean switchToChild = !FamilyMemberRoleEnum.CHILD.equals(originRole)
                && FamilyMemberRoleEnum.CHILD.equals(targetRole);

        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getId, targetMember.getId())
                .set(AuthFamilyMemberEntity::getRole, targetRole)
                .set(switchToChild, AuthFamilyMemberEntity::getDefaultShared, BoolEnum.TRUE.getCode())
                .set(switchToChild, AuthFamilyMemberEntity::getQueryOnlyMyself, BoolEnum.TRUE.getCode())
                .update();
    }

    @Override
    public Integer myDefaultShared(Integer groupId) {
        Integer userId = UserUtils.getUserId();
        AuthFamilyMemberEntity memberEntity = queryNormalMember(groupId, userId);
        if (memberEntity == null) {
            throw new BusinessException("您不是该家庭组成员");
        }

        return this.resolveDefaultShared(memberEntity);
    }

    @Override
    @Transactional
    public void updateMyDefaultShared(FamilyMemberDefaultSharedUpdateDto dto) {
        Integer userId = UserUtils.getUserId();
        AuthFamilyMemberEntity memberEntity = queryNormalMember(dto.getGroupId(), userId);
        if (memberEntity == null) {
            throw new BusinessException("您不是该家庭组成员");
        }
        if (FamilyMemberRoleEnum.CHILD.equals(memberEntity.getRole())) {
            throw new BusinessException("儿童角色不能修改默认共享开关");
        }

        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getId, memberEntity.getId())
                .set(AuthFamilyMemberEntity::getDefaultShared, dto.getDefaultShared())
                .update();
    }

    @Override
    @Transactional
    public void updateMyQueryScope(FamilyMemberQueryScopeUpdateDto dto) {
        Integer userId = UserUtils.getUserId();
        AuthFamilyMemberEntity memberEntity = queryNormalMember(dto.getGroupId(), userId);
        if (memberEntity == null) {
            throw new BusinessException("您不是该家庭组成员");
        }
        if (FamilyMemberRoleEnum.CHILD.equals(memberEntity.getRole())) {
            throw new BusinessException("儿童角色只能查看自己的数据");
        }

        familyMemberDao.lambdaUpdate()
                .eq(AuthFamilyMemberEntity::getId, memberEntity.getId())
                .set(AuthFamilyMemberEntity::getQueryOnlyMyself, dto.getQueryOnlyMyself())
                .update();
    }

    @Override
    public Integer queryDefaultShared(Integer userId) {
        return this.querySharedSavePolicy(userId).getDefaultShared();
    }

    @Override
    public Integer queryCurrentGroupId(Integer userId) {
        AuthUserEntity userEntity = authUserDao.getById(userId);
        if (userEntity == null || userEntity.getFamilyGroupId() == null) {
            return 0;
        }
        return userEntity.getFamilyGroupId();
    }

    @Override
    public FamilySharedSavePolicyVo querySharedSavePolicy(Integer userId) {
        FamilySharedSavePolicyVo vo = new FamilySharedSavePolicyVo();
        Integer currentGroupId = this.queryCurrentGroupId(userId);
        vo.setCurrentGroupId(currentGroupId);
        vo.setDefaultShared(BoolEnum.FALSE.getCode());
        vo.setForceShared(BoolEnum.FALSE.getCode());
        if (currentGroupId == null || currentGroupId <= 0) {
            return vo;
        }

        AuthFamilyMemberEntity memberEntity = queryNormalMember(currentGroupId, userId);
        if (memberEntity == null) {
            return vo;
        }

        vo.setDefaultShared(this.resolveDefaultShared(memberEntity));
        vo.setForceShared(FamilyMemberRoleEnum.CHILD.equals(memberEntity.getRole())
                ? BoolEnum.TRUE.getCode()
                : BoolEnum.FALSE.getCode());
        return vo;
    }

    @Override
    public FamilySharedQueryPolicyVo querySharedQueryPolicy(Integer userId) {
        FamilySharedQueryPolicyVo vo = new FamilySharedQueryPolicyVo();
        Integer currentGroupId = this.queryCurrentGroupId(userId);
        vo.setCurrentGroupId(currentGroupId);
        vo.setForceQueryOnlyMyself(BoolEnum.FALSE.getCode());
        if (currentGroupId == null || currentGroupId <= 0) {
            return vo;
        }

        AuthFamilyMemberEntity memberEntity = queryNormalMember(currentGroupId, userId);
        if (memberEntity == null) {
            return vo;
        }

        vo.setForceQueryOnlyMyself(FamilyMemberRoleEnum.CHILD.equals(memberEntity.getRole())
                ? BoolEnum.TRUE.getCode()
                : BoolEnum.FALSE.getCode());
        this.resolveQueryOnlyMyself(memberEntity);
        return vo;
    }

    private Integer resolveDefaultShared(AuthFamilyMemberEntity memberEntity) {
        Integer defaultShared = memberEntity.getDefaultShared();
        if (defaultShared == null) {
            defaultShared = BoolEnum.FALSE.getCode();
        }
        if (FamilyMemberRoleEnum.CHILD.equals(memberEntity.getRole())
                && !BoolEnum.TRUE.getCode().equals(defaultShared)) {
            familyMemberDao.lambdaUpdate()
                    .eq(AuthFamilyMemberEntity::getId, memberEntity.getId())
                    .set(AuthFamilyMemberEntity::getDefaultShared, BoolEnum.TRUE.getCode())
                    .update();
            return BoolEnum.TRUE.getCode();
        }
        return defaultShared;
    }

    private Integer resolveQueryOnlyMyself(AuthFamilyMemberEntity memberEntity) {
        Integer queryOnlyMyself = memberEntity.getQueryOnlyMyself();
        if (queryOnlyMyself == null) {
            queryOnlyMyself = BoolEnum.FALSE.getCode();
        }
        if (FamilyMemberRoleEnum.CHILD.equals(memberEntity.getRole())
                && !BoolEnum.TRUE.getCode().equals(queryOnlyMyself)) {
            familyMemberDao.lambdaUpdate()
                    .eq(AuthFamilyMemberEntity::getId, memberEntity.getId())
                    .set(AuthFamilyMemberEntity::getQueryOnlyMyself, BoolEnum.TRUE.getCode())
                    .update();
            return BoolEnum.TRUE.getCode();
        }
        return queryOnlyMyself;
    }

    /**
     * 校验群主权限
     *
     * @param groupId 家庭组ID
     */
    private void checkOwnerPermission(Integer groupId) {
        checkRolePermission(groupId, FamilyMemberRoleEnum.OWNER);
    }

    /**
     * 校验成员权限
     *
     * @param groupId 家庭组ID
     */
    private void checkMemberPermission(Integer groupId) {
        checkRolePermission(groupId, FamilyMemberRoleEnum.OWNER, FamilyMemberRoleEnum.PARENT,
                FamilyMemberRoleEnum.MEMBER, FamilyMemberRoleEnum.CHILD);
    }

    /**
     * 校验角色权限
     *
     * @param groupId       家庭组ID
     * @param requiredRoles 允许执行的角色
     * @return 当前用户成员记录
     */
    private AuthFamilyMemberEntity checkRolePermission(Integer groupId, FamilyMemberRoleEnum... requiredRoles) {
        Integer userId = UserUtils.getUserId();
        AuthFamilyMemberEntity memberEntity = queryNormalMember(groupId, userId);
        if (memberEntity == null) {
            throw new BusinessException("您不是该家庭组成员");
        }

        for (FamilyMemberRoleEnum requiredRole : requiredRoles) {
            if (requiredRole.equals(memberEntity.getRole())) {
                return memberEntity;
            }
        }
        throw new BusinessException("您暂无此操作权限");
    }

    /**
     * 查询用户在指定家庭组中的有效成员记录
     */
    private AuthFamilyMemberEntity queryNormalMember(Integer groupId, Integer userId) {
        return familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getGroupId, groupId)
                .eq(AuthFamilyMemberEntity::getUserId, userId)
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .one();
    }

    /**
     * 校验用户当前未加入任何家庭组
     */
    private void ensureNoActiveGroup(Integer userId) {
        Long count = familyMemberDao.lambdaQuery()
                .eq(AuthFamilyMemberEntity::getUserId, userId)
                .eq(AuthFamilyMemberEntity::getStatus, FamilyMemberStatusEnum.NORMAL)
                .count();
        if (count > 0) {
            throw new BusinessException("您已加入家庭组，请先退出当前家庭组");
        }
    }

    /**
     * 发送家庭组成员离组消息
     */
    private void sendFamilyMemberLeaveMessage(Integer groupId, List<Integer> userIdList) {
        if (groupId == null || groupId <= 0 || userIdList == null || userIdList.isEmpty()) {
            return;
        }
        FamilyGroupMemberLeaveMqDto mqDto = new FamilyGroupMemberLeaveMqDto();
        mqDto.setGroupId(groupId);
        mqDto.setUserIdList(userIdList);
        MQProducerHelper.send(FamilyGroupTopicEnum.MEMBER_LEAVE, mqDto);
    }
}
