package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.RegisterFormDto;
import com.itwray.iw.auth.service.AuthRegisterService;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.web.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证注册的接口控制层
 *
 * @author wray
 * @since 2024/3/5
 */
@RestController
@RequestMapping("/register")
@AllArgsConstructor
@Validated
@Tag(name = "认证注册接口")
public class AuthRegisterController {

    private final AuthRegisterService authRegisterService;

    @PostMapping("/form")
    @Operation(summary = "根据表单注册")
    public void registerByForm(@RequestBody @Valid RegisterFormDto dto, HttpServletRequest request) {
        authRegisterService.registerByForm(dto, IpUtils.getClientIp(request));
    }

    @GetMapping("/getVerificationCode")
    @Operation(summary = "获取验证码")
    public GeneralResponse<String> getVerificationCode(@RequestParam("phoneNumber") String phoneNumber, HttpServletRequest request) {
        return GeneralResponse.success(authRegisterService.getVerificationCode(phoneNumber, IpUtils.getClientIp(request)));
    }
}
