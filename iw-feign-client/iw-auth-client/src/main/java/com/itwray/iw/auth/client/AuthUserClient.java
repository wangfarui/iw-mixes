package com.itwray.iw.auth.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 用户接口
 *
 * @author farui.wang
 * @since 2025/7/18
 */
@FeignClient(value = "iw-auth-service", path = "/auth-service/user")
public interface AuthUserClient {


}
