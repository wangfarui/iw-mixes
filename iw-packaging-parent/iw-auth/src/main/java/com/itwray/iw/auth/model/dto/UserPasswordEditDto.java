package com.itwray.iw.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "旧密码")
    private String oldPassword;

    @Schema(title = "新密码")
    private String newPassword;
}
