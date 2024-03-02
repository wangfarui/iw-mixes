package com.itwray.iw.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录-账号密码方式 DTO
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Data
@ApiModel("登录-账号密码方式DTO")
public class LoginPasswordDto {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;
}
