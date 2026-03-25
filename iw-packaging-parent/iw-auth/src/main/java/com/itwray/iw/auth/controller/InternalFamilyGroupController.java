package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.vo.FamilySharedQueryPolicyVo;
import com.itwray.iw.auth.model.vo.FamilySharedSavePolicyVo;
import com.itwray.iw.auth.service.AuthFamilyGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 家庭组内部接口
 *
 * @author wray
 * @since 2026/3/11
 */
@RestController
@RequestMapping("/internal/family/group")
@Validated
@Tag(name = "家庭组内部接口")
public class InternalFamilyGroupController {

    private final AuthFamilyGroupService authFamilyGroupService;

    @Autowired
    public InternalFamilyGroupController(AuthFamilyGroupService authFamilyGroupService) {
        this.authFamilyGroupService = authFamilyGroupService;
    }

    @GetMapping("/defaultShared")
    @Operation(summary = "查询用户当前家庭组默认共享开关")
    public Integer defaultShared(@RequestParam("userId") Integer userId) {
        return authFamilyGroupService.queryDefaultShared(userId);
    }

    @GetMapping("/currentGroupId")
    @Operation(summary = "查询用户当前家庭组ID")
    public Integer currentGroupId(@RequestParam("userId") Integer userId) {
        return authFamilyGroupService.queryCurrentGroupId(userId);
    }

    @GetMapping("/sharedSavePolicy")
    @Operation(summary = "查询用户当前共享保存策略")
    public FamilySharedSavePolicyVo sharedSavePolicy(@RequestParam("userId") Integer userId) {
        return authFamilyGroupService.querySharedSavePolicy(userId);
    }

    @GetMapping("/sharedQueryPolicy")
    @Operation(summary = "查询用户当前共享查询策略")
    public FamilySharedQueryPolicyVo sharedQueryPolicy(@RequestParam("userId") Integer userId) {
        return authFamilyGroupService.querySharedQueryPolicy(userId);
    }
}
