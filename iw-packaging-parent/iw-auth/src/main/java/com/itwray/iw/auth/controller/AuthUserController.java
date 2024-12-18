package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.UserPasswordEditDto;
import com.itwray.iw.auth.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户的接口控制层
 *
 * @author wray
 * @since 2024/8/22
 */
@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "用户接口")
public class AuthUserController {

    private AuthUserService authUserService;

    @Autowired
    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/editAvatar")
    @Operation(summary = "编辑用户头像")
    public void editAvatar(@RequestBody Map<String, Object> body) {
        String avatar = (String) body.get("avatar");
        authUserService.editAvatar(avatar);
    }

    @PostMapping("/editPassword")
    @Operation(summary = "修改用户密码")
    public void editPassword(@RequestBody UserPasswordEditDto dto) {
        authUserService.editPassword(dto);
    }
}
