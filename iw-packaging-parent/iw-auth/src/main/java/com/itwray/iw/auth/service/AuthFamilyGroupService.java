package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.*;
import com.itwray.iw.auth.model.vo.FamilyGroupDetailVo;
import com.itwray.iw.auth.model.vo.FamilyInviteVo;
import com.itwray.iw.auth.model.vo.FamilyMemberVo;
import com.itwray.iw.auth.model.vo.FamilySharedQueryPolicyVo;
import com.itwray.iw.auth.model.vo.FamilySharedSavePolicyVo;
import com.itwray.iw.web.service.WebService;

import java.util.List;

/**
 * 家庭组 服务接口
 *
 * @author wray
 * @since 2024-03-10
 */
public interface AuthFamilyGroupService extends WebService<FamilyGroupAddDto, FamilyGroupUpdateDto, FamilyGroupDetailVo, Integer> {

    /**
     * 生成邀请码
     *
     * @param dto 生成邀请码DTO
     * @return 邀请码信息
     */
    FamilyInviteVo generateInvite(FamilyInviteGenerateDto dto);

    /**
     * 验证邀请码
     *
     * @param inviteCode 邀请码
     * @return 邀请码信息（包含家庭组信息）
     */
    FamilyInviteVo validateInvite(String inviteCode);

    /**
     * 查询邀请码列表
     *
     * @param groupId 家庭组ID
     * @return 邀请码列表
     */
    List<FamilyInviteVo> inviteList(Integer groupId);

    /**
     * 加入家庭组
     *
     * @param dto 加入家庭组DTO
     */
    void join(FamilyGroupJoinDto dto);

    /**
     * 退出家庭组
     *
     * @param groupId 家庭组ID
     */
    void quit(Integer groupId);

    /**
     * 移除成员
     *
     * @param dto 移除成员DTO
     */
    void removeMember(FamilyMemberRemoveDto dto);

    /**
     * 查询成员列表
     *
     * @param groupId 家庭组ID
     * @return 成员列表
     */
    List<FamilyMemberVo> memberList(Integer groupId);

    /**
     * 获取我的家庭组
     *
     * @return 家庭组信息（个人模式返回null）
     */
    FamilyGroupDetailVo myGroup();

    /**
     * 转让群主
     *
     * @param dto 转让群主DTO
     */
    void transferOwner(FamilyGroupTransferDto dto);

    /**
     * 分配成员角色
     *
     * @param dto 角色分配DTO
     */
    void assignRole(FamilyMemberRoleAssignDto dto);

    /**
     * 查询当前用户在指定家庭组下的默认共享开关
     *
     * @param groupId 家庭组ID
     * @return 默认共享开关(0关闭 1开启)
     */
    Integer myDefaultShared(Integer groupId);

    /**
     * 更新当前用户在指定家庭组下的默认共享开关
     *
     * @param dto 默认共享开关更新DTO
     */
    void updateMyDefaultShared(FamilyMemberDefaultSharedUpdateDto dto);

    /**
     * 更新当前用户在指定家庭组下的共享数据查看范围
     *
     * @param dto 共享数据查看范围更新DTO
     */
    void updateMyQueryScope(FamilyMemberQueryScopeUpdateDto dto);

    /**
     * 查询指定用户当前家庭组下的默认共享开关
     *
     * @param userId 用户ID
     * @return 默认共享开关(0关闭 1开启)
     */
    Integer queryDefaultShared(Integer userId);

    /**
     * 查询指定用户当前家庭组ID
     *
     * @param userId 用户ID
     * @return 家庭组ID(0表示个人模式)
     */
    Integer queryCurrentGroupId(Integer userId);

    /**
     * 查询指定用户的共享保存策略
     *
     * @param userId 用户ID
     * @return 共享保存策略
     */
    FamilySharedSavePolicyVo querySharedSavePolicy(Integer userId);

    /**
     * 查询指定用户的共享查询策略
     *
     * @param userId 用户ID
     * @return 共享查询策略
     */
    FamilySharedQueryPolicyVo querySharedQueryPolicy(Integer userId);
}
