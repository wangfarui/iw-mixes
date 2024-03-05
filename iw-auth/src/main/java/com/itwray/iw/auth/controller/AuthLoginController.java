package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权登录的接口控制层
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@RestController
@RequestMapping("/login")
@AllArgsConstructor
@Validated
@Tag(name = "授权登录接口")
public class AuthLoginController {

    private final AuthUserService authUserService;

    @PostMapping("/password")
    @Operation(summary = "根据账号密码登录")
    public UserInfoVo loginByPassword(@RequestBody @Valid LoginPasswordDto dto) {
        return authUserService.loginByPassword(dto);
    }
}
