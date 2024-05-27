package com.itwray.iw.eat.controller;

import com.itwray.iw.eat.service.EatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户接口控制层
 *
 * @author wray
 * @since 2024/5/27
 */
@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "用户接口")
public class EatUserController {

    @Resource
    private EatUserService eatUserService;

    @PostMapping("/editAvatar")
    @Operation(summary = "编辑头像")
    public void editAvatar(@RequestBody Map<String, Object> body) {
        String avatar = (String) body.get("avatar");
        eatUserService.editAvatar(avatar);
    }
}
