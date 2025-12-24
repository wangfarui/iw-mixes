package com.itwray.iw.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户接口
 *
 * @author farui.wang
 * @since 2025/7/18
 */
@FeignClient(value = "iw-auth-service", contextId = "userClient", path = "/internal/user")
public interface AuthUserClient {

    @GetMapping("/genericUserToken")
    String genericUserToken(@RequestParam("userId") Integer userId);
}
