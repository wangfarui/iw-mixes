package com.itwray.iw.eat.model.vo;

import lombok.AllArgsConstructor;
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
}
