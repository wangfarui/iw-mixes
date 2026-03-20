package com.itwray.iw.auth.client;

import com.itwray.iw.auth.model.vo.UserSimpleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @PostMapping("/simpleList")
    List<UserSimpleVo> querySimpleUserList(@RequestBody List<Integer> userIdList);
}
