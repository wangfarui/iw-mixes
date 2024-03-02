package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.dto.LoginPasswordDto;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.auth.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
@Api("授权登录接口")
public class AuthLoginController {

    private final AuthUserService authUserService;

    @PostMapping("/password")
    @ApiOperation("根据账号密码登录")
    public UserInfoVo loginByPassword(@RequestBody LoginPasswordDto dto) {
        return authUserService.loginByPassword(dto);
    }
}
