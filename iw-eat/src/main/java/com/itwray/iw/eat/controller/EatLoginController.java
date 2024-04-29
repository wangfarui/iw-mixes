package com.itwray.iw.eat.controller;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.eat.model.dto.UserLoginDto;
import com.itwray.iw.eat.model.vo.UserLoginVo;
import com.itwray.iw.eat.service.EatUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class EatLoginController {

    @Resource
    private EatUserService eatUserService;

    @PostMapping("/doLogin")
    public GeneralResponse<UserLoginVo> doLogin(@RequestBody UserLoginDto dto) {
        return GeneralResponse.success(eatUserService.doLogin(dto));
    }
}
