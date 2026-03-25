package com.itwray.iw.auth.client;

import com.itwray.iw.auth.model.vo.FamilySharedQueryPolicyVo;
import com.itwray.iw.auth.model.vo.FamilySharedSavePolicyVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 家庭组接口
 *
 * @author wray
 * @since 2026/3/11
 */
@FeignClient(value = "iw-auth-service", contextId = "familyGroupClient", path = "/internal/family/group")
public interface AuthFamilyGroupClient {

    @GetMapping("/defaultShared")
    @Operation(summary = "查询用户当前家庭组默认共享开关")
    Integer queryDefaultShared(@RequestParam("userId") Integer userId);

    @GetMapping("/currentGroupId")
    @Operation(summary = "查询用户当前家庭组ID")
    Integer queryCurrentGroupId(@RequestParam("userId") Integer userId);

    @GetMapping("/sharedSavePolicy")
    @Operation(summary = "查询用户当前共享保存策略")
    FamilySharedSavePolicyVo querySharedSavePolicy(@RequestParam("userId") Integer userId);

    @GetMapping("/sharedQueryPolicy")
    @Operation(summary = "查询用户当前共享查询策略")
    FamilySharedQueryPolicyVo querySharedQueryPolicy(@RequestParam("userId") Integer userId);
}
