package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.LoginPasswordDto;
import com.itwray.iw.auth.model.dto.LoginVerificationCodeDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 认证登录的接口控制层
 *
 * @author wray
 * @since 2024/3/2
 */
@RestController
@RequestMapping("/login")
@Validated
@Tag(name = "认证登录接口")
public class AuthLoginController {

    private final AuthUserService authUserService;

    @Value("${captcha.width:130}")
    private Integer captchaWidth;

    @Value("${captcha.height:48}")
    private Integer captchaHeight;

    @Value("${captcha.len:4}")
    private Integer captchaLen;

    @Autowired
    public AuthLoginController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping("/captcha.jpg")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        CaptchaJakartaUtil.out(this.captchaWidth, this.captchaHeight, this.captchaLen, Captcha.TYPE_ONLY_NUMBER, request, response);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public void logout() {
        authUserService.logout();
    }

    @PostMapping("/password")
    @Operation(summary = "根据账号密码登录")
    public UserInfoVo loginByPassword(@RequestBody @Valid LoginPasswordDto dto) {
        return authUserService.loginByPassword(dto);
    }

    @PostMapping("/verificationCode")
    @Operation(summary = "根据验证码登录")
    public UserInfoVo loginByVerificationCode(@RequestBody @Valid LoginVerificationCodeDto dto) {
        return authUserService.loginByVerificationCode(dto);
    }
}
