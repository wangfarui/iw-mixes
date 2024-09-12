package com.itwray.iw.common.constants;

import lombok.Getter;

/**
 * 启用状态枚举
 *
 * @author wray
 * @since 2024/9/12
 */
@Getter
public enum EnableEnums {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private final Integer code;

    private final String name;

    EnableEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
