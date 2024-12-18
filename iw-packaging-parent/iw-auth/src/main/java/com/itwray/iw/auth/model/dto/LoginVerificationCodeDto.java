package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 登录-电话号码验证码方式 DTO
 *
 * @author wray
 * @see RegisterFormDto
 * @since 2024/3/2
 */
@Data
@Schema(name = "登录-电话号码验证码方式")
public class LoginVerificationCodeDto {

    @Schema(title = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phoneNumber;

    @Schema(title = "验证码")
    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码固定为6位数字")
    private String verificationCode;
}
