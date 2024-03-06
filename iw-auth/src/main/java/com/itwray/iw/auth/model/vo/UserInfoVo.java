package com.itwray.iw.auth.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息 VO
 *
 * @author wray
 * @since 2024/3/2
 */
@Data
@Schema(name = "用户信息VO")
public class UserInfoVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(title = "用户id")
    private Long id;

    @Schema(title = "账号")
    private String account;

    @Schema(title = "用户名")
    private String username;
}
