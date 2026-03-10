package com.itwray.iw.auth.service;

import com.itwray.iw.auth.model.dto.*;
import com.itwray.iw.auth.model.vo.FamilyGroupDetailVo;
import com.itwray.iw.auth.model.vo.FamilyInviteVo;
import com.itwray.iw.auth.model.vo.FamilyMemberVo;
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
}
