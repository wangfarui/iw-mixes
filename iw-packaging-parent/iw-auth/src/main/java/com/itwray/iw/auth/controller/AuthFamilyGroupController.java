package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.*;
import com.itwray.iw.auth.model.vo.FamilyGroupDetailVo;
import com.itwray.iw.auth.model.vo.FamilyInviteVo;
import com.itwray.iw.auth.model.vo.FamilyMemberVo;
import com.itwray.iw.auth.service.AuthFamilyGroupService;
import com.itwray.iw.web.controller.WebController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 家庭组 接口控制层
 *
 * @author wray
 * @since 2024-03-10
 */
@RestController
@RequestMapping("/family/group")
@Validated
@Tag(name = "家庭组管理")
public class AuthFamilyGroupController extends WebController<AuthFamilyGroupService,
        FamilyGroupAddDto, FamilyGroupUpdateDto, FamilyGroupDetailVo, Integer> {

    @Autowired
    public AuthFamilyGroupController(AuthFamilyGroupService webService) {
        super(webService);
    }

    @GetMapping("/myGroup")
    @Operation(summary = "获取我的家庭组")
    public FamilyGroupDetailVo myGroup() {
        return getWebService().myGroup();
    }

    @PostMapping("/generateInvite")
    @Operation(summary = "生成邀请码")
    public FamilyInviteVo generateInvite(@RequestBody @Valid FamilyInviteGenerateDto dto) {
        return getWebService().generateInvite(dto);
    }

    @GetMapping("/validateInvite")
    @Operation(summary = "验证邀请码")
    public FamilyInviteVo validateInvite(
            @Parameter(description = "邀请码", required = true)
            @RequestParam String inviteCode) {
        return getWebService().validateInvite(inviteCode);
    }

    @GetMapping("/inviteList")
    @Operation(summary = "查询邀请码列表")
    public List<FamilyInviteVo> inviteList(
            @Parameter(description = "家庭组ID", required = true)
            @RequestParam Integer groupId) {
        return getWebService().inviteList(groupId);
    }

    @PostMapping("/join")
    @Operation(summary = "加入家庭组")
    public void join(@RequestBody @Valid FamilyGroupJoinDto dto) {
        getWebService().join(dto);
    }

    @PostMapping("/quit")
    @Operation(summary = "退出家庭组")
    public void quit(
            @Parameter(description = "家庭组ID", required = true)
            @RequestParam Integer groupId) {
        getWebService().quit(groupId);
    }

    @PostMapping("/removeMember")
    @Operation(summary = "移除成员")
    public void removeMember(@RequestBody @Valid FamilyMemberRemoveDto dto) {
        getWebService().removeMember(dto);
    }

    @GetMapping("/memberList")
    @Operation(summary = "查询成员列表")
    public List<FamilyMemberVo> memberList(
            @Parameter(description = "家庭组ID", required = true)
            @RequestParam Integer groupId) {
        return getWebService().memberList(groupId);
    }

    @PostMapping("/transferOwner")
    @Operation(summary = "转让群主")
    public void transferOwner(@RequestBody @Valid FamilyGroupTransferDto dto) {
        getWebService().transferOwner(dto);
    }

    @PostMapping("/assignRole")
    @Operation(summary = "分配成员角色")
    public void assignRole(@RequestBody @Valid FamilyMemberRoleAssignDto dto) {
        getWebService().assignRole(dto);
    }

    @GetMapping("/myDefaultShared")
    @Operation(summary = "查询我的默认共享开关")
    public Integer myDefaultShared(
            @Parameter(description = "家庭组ID", required = true)
            @RequestParam Integer groupId) {
        return getWebService().myDefaultShared(groupId);
    }

    @PostMapping("/updateMyDefaultShared")
    @Operation(summary = "更新我的默认共享开关")
    public void updateMyDefaultShared(@RequestBody @Valid FamilyMemberDefaultSharedUpdateDto dto) {
        getWebService().updateMyDefaultShared(dto);
    }

    @PostMapping("/updateMyQueryScope")
    @Operation(summary = "更新我的共享数据查看范围")
    public void updateMyQueryScope(@RequestBody @Valid FamilyMemberQueryScopeUpdateDto dto) {
        getWebService().updateMyQueryScope(dto);
    }
}
