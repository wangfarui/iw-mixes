package com.itwray.iw.eat.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itwray.iw.web.json.serialize.DefaultImageSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录响应对象
 *
 * @author wray
 * @since 2024/4/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginVo {

    /**
     * 姓名
     */
    private String name;

    /**
     * sa token
     */
    private String tokenName;

    /**
     * sa token
     */
    private String tokenValue;

    /**
     * 头像（url地址）
     */
    @JsonSerialize(using = DefaultImageSerializer.class)
    private String avatar;
}
