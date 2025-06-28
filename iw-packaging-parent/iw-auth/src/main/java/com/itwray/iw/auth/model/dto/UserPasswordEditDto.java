package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户信息 修改密码DTO
 *
 * @author wray
 * @since 2024/12/17
 */
@Data
@Schema(name = "用户信息 修改密码DTO")
public class UserPasswordEditDto {

    @Schema(title = "手机验证码")
    private String verificationCode;

    @Schema(title = "邮箱验证码")
    private String emailVerificationCode;

    @Schema(title = "旧密码")
    private String oldPassword;

    @Schema(title = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
