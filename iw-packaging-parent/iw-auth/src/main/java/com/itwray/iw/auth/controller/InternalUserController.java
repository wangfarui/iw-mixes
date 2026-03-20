package com.itwray.iw.auth.controller;

import com.itwray.iw.auth.model.vo.UserSimpleVo;
import com.itwray.iw.auth.service.AuthUserService;
import com.itwray.iw.common.GeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户内部接口
 *
 * @author farui.wang
 * @since 2025/7/18
 */
@RestController
@RequestMapping("/internal/user")
@Validated
@Tag(name = "用户内部接口")
public class InternalUserController {

    private final AuthUserService authUserService;

    @Autowired
    public InternalUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping("/genericUserToken")
    @Operation(summary = "生成用户token")
    public GeneralResponse<String> genericUserToken(@RequestParam("userId") Integer userId) {
        String token = authUserService.genericUserToken(userId);
        return GeneralResponse.success(token);
    }

    @PostMapping("/simpleList")
    @Operation(summary = "批量查询用户精简信息")
    public List<UserSimpleVo> simpleList(@RequestBody List<Integer> userIdList) {
        return authUserService.querySimpleUserList(userIdList);
    }
}
