package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 用户登录方式枚举
 *
 * @author farui.wang
 * @since 2025/5/29
 */
@Getter
public enum UserLoginWayEnum implements BusinessConstantEnum {

    PHONE(1, "电话号码登录"),
    EMAIL(2, "邮箱登录"),
    ;

    private final Integer code;

    private final String name;

    UserLoginWayEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
