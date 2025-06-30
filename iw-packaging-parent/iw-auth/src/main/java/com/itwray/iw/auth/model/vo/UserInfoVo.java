package com.itwray.iw.auth.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itwray.iw.web.json.serialize.DefaultImageSerializer;
import com.itwray.iw.web.json.serialize.EmailAddressSerializer;
import com.itwray.iw.web.json.serialize.PhoneNumberSerializer;
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

    @Schema(title = "用户id")
    private Integer id;

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "token key")
    private String tokenName;

    @Schema(title = "token value")
    private String tokenValue;

    @Schema(title = "头像（url地址）")
    @JsonSerialize(using = DefaultImageSerializer.class)
    private String avatar = "";

    @Schema(title = "电话号码")
    @JsonSerialize(using = PhoneNumberSerializer.class)
    private String phoneNumber;

    @Schema(title = "邮箱地址")
    @JsonSerialize(using = EmailAddressSerializer.class)
    private String emailAddress;

    @Schema(title = "新用户")
    private Boolean newUser;
}
