package com.itwray.iw.eat.model.dto;

import lombok.Data;

/**
 * 用户登录信息
 *
 * @author wray
 * @since 2024/4/29
 */
@Data
public class UserLoginDto {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
