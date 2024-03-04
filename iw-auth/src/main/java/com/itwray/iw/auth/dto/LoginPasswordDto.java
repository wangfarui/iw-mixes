package com.itwray.iw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录-账号密码方式 DTO
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Data
@Schema(name = "登录-账号密码方式DTO")
public class LoginPasswordDto {

    @Schema(title = "账号")
    private String account;

    @Schema(title = "密码")
    private String password;
}
