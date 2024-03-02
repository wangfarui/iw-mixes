package com.itwray.iw.auth.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息 VO
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Data
@ApiModel("用户信息VO")
public class UserInfoVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("用户名")
    private String username;
}
