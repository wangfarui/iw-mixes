package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.dto.RegisterFormDto;
import com.itwray.iw.auth.service.AuthUserService;
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
 * 授权注册的接口控制层
 *
 * @author wangfarui
 * @since 2024/3/5
 */
@RestController
@RequestMapping("/register")
@AllArgsConstructor
@Validated
@Tag(name = "授权注册接口")
public class AuthRegisterController {

    private final AuthUserService authUserService;

    @PostMapping("/form")
    @Operation(summary = "根据表单注册")
    public void registerByForm(@RequestBody @Valid RegisterFormDto dto) {
        authUserService.registerByForm(dto);
    }
}
