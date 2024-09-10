package com.itwray.iw.web.model.enums;

import lombok.Getter;

/**
 * 字典类型枚举
 *
 * @author wray
 * @since 2024/9/10
 */
@Getter
public enum DictTypeEnum {

    /** iw-web **/
    WEB_TAG(1001, "web模块"),

    /** iw-auth **/
    AUTH_TAG(2001, "授权模块"),

    /** iw-eat **/
    EAT_TAG(3001, "餐饮模块"),

    /** iw-bookkeeping **/
    BOOKKEEPING_TAG(4001, "记账标签"),
    ;

    private final Integer code;

    private final String name;

    DictTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
