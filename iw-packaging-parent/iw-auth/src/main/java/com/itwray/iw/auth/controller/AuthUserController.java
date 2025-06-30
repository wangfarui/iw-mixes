package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.dto.UserInfoEditDto;
import com.itwray.iw.auth.model.dto.UserPasswordEditDto;
import com.itwray.iw.auth.model.dto.UserUsernameEditDto;
import com.itwray.iw.auth.model.vo.UserInfoVo;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.common.GeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    private final AuthUserService authUserService;

    @Autowired
    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/editPassword")
    @Operation(summary = "修改用户密码")
    public void editPassword(@RequestBody @Valid UserPasswordEditDto dto) {
        authUserService.editPassword(dto);
    }

    @GetMapping("/getVerificationCode")
    @Operation(summary = "根据操作行为获取验证码")
    public void getVerificationCodeByAction(@RequestParam("action") Integer action) {
        authUserService.getVerificationCodeByAction(action);
    }

    @PutMapping("/editUsername")
    @Operation(summary = "修改用户名")
    public void editUsername(@RequestBody @Valid UserUsernameEditDto dto) {
        authUserService.editUsername(dto);
    }

    @PutMapping("/editUserInfo")
    @Operation(summary = "修改用户信息")
    public void editUserInfo(@RequestBody @Valid UserInfoEditDto dto) {
        authUserService.editUserInfo(dto);
    }

    @GetMapping("/answer")
    public GeneralResponse<String> aiAnswer(@RequestParam("t") String content) {
        return GeneralResponse.success(authUserService.aiAnswer(content));
    }

    @GetMapping("/getUserInfo")
    public UserInfoVo getUserInfo() {
        return authUserService.getUserInfo();
    }
}
