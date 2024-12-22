package com.itwray.iw.common.constants;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 布尔枚举
 *
 * @author wray
 * @since 2024/4/25
 */
@Getter
public enum BoolEnum implements ConstantEnum {
    FALSE(0, "否"),
    TRUE(1, "是");

    private final Integer code;

    private final String name;

    BoolEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
