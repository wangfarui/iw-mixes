package com.itwray.iw.web.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Auth服务的权限认证Client
 *
 * @author wray
 * @since 2024/9/9
 */
@FeignClient(value = "iw-auth-service", path = "/auth-service/authentication")
public interface AuthenticationClient {

    @GetMapping("/validateToken")
    @Operation(summary = "校验Token有效性")
    Boolean validateToken(@RequestParam("token") String token);

    @GetMapping("/getUserIdByToken")
    @Operation(summary = "获取指定token的用户id")
    Integer getUserIdByToken(@RequestParam("token") String token);
}
