package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限认证的接口控制层
 *
 * @author wray
 * @since 2024/8/21
 */
@RestController
@RequestMapping("/authentication")
@Validated
@Tag(name = "权限认证接口")
public class AuthenticationController {

    private final AuthUserService authUserService;

    @Autowired
    public AuthenticationController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping("/validateToken")
    @Operation(summary = "校验Token有效性")
    public Boolean validateToken(@RequestParam("token") String token) {
        return authUserService.validateToken(token);
    }
}
