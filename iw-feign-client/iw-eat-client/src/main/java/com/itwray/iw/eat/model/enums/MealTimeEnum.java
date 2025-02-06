package com.itwray.iw.eat.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 用餐时间枚举
 *
 * @author wray
 * @since 2024/5/10
 */
@Getter
public enum MealTimeEnum implements BusinessConstantEnum {
    ANY(0, "任意时间"),
    BREAKFAST(1, "早餐"),
    NOONING(2, "午餐"),
    DINNER(3, "晚餐");

    private final Integer code;

    private final String name;

    MealTimeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
