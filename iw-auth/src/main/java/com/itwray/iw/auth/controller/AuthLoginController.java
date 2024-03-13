package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import io.springboot.captcha.base.Captcha;
import io.springboot.captcha.utils.CaptchaJakartaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 授权登录的接口控制层
 *
 * @author wray
 * @since 2024/3/2
 */
@RestController
@RequestMapping("/login")
@AllArgsConstructor
@Validated
@Tag(name = "授权登录接口")
public class AuthLoginController {

    private final AuthUserService authUserService;

    @GetMapping("/captcha.jpg")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaJakartaUtil.out(130, 48, 4, Captcha.TYPE_ONLY_NUMBER, request, response);
    }

    @PostMapping("/password")
    @Operation(summary = "根据账号密码登录")
    public UserInfoVo loginByPassword(@RequestBody @Valid LoginPasswordDto dto) {
        return authUserService.loginByPassword(dto);
    }
}
