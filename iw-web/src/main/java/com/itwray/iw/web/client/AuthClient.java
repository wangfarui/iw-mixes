package com.itwray.iw.web.client;

import com.itwray.iw.common.GeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Auth服务Client
 *
 * @author wray
 * @since 2024/9/9
 */
@FeignClient(value = "iw-auth-service", path = "auth-service")
public interface AuthClient {

    @GetMapping("/authentication/getUserIdByToken")
    @Operation(summary = "获取指定token的用户id")
    GeneralResponse<Integer> getUserIdByToken(@RequestParam("token") String token);
}
