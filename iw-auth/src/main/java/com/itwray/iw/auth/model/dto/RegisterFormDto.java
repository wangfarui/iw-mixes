package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册-表单填写方式 DTO
 *
 * @author wangfarui
 * @since 2024/3/5
 */
@Data
@Schema(name = "注册-表单填写方式DTO")
public class RegisterFormDto {

    @Schema(title = "账号")
    @NotBlank(message = "账号不能为空")
    @Size(max = 32, message = "账号不能超过32位")
    private String account;

    @Schema(title = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(max = 64, message = "用户名不能超过64位")
    private String username;

    @Schema(title = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(max = 64, message = "密码不能超过64位")
    private String password;
}
