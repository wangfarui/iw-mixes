package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录-用户名密码方式 DTO
 *
 * @author wray
 * @since 2024/3/2
 */
@Data
@Schema(name = "登录-用户名密码方式DTO")
public class LoginPasswordDto {

    @Schema(title = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(title = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
