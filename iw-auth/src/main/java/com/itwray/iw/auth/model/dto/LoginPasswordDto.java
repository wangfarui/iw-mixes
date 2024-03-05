package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(title = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
