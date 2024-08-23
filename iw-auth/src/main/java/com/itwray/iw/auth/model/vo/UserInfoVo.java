package com.itwray.iw.auth.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itwray.iw.web.json.serialize.DefaultImageSerializer;
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

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "token key")
    private String tokenName;

    @Schema(title = "token value")
    private String tokenValue;

    @Schema(title = "头像（url地址）")
    @JsonSerialize(using = DefaultImageSerializer.class)
    private String avatar;
}
