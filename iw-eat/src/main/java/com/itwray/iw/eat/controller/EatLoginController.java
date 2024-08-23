package com.itwray.iw.eat.controller;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.eat.model.dto.UserLoginDto;
import com.itwray.iw.eat.model.vo.UserLoginVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口
 *
 * @author wray
 * @since 2024/4/29
 */
@RestController
@RequestMapping("/login")
@Validated
@Tag(name = "登录接口")
@RefreshScope
public class EatLoginController {


    @Value(value = "${login.user-name:}")
    private String userName;

    @PostMapping("/doLogin")
    public GeneralResponse<UserLoginVo> doLogin(@RequestBody UserLoginDto dto) {
        return GeneralResponse.success();
    }

    @GetMapping("/get")
    public String get() {
        return this.userName;
    }
}
