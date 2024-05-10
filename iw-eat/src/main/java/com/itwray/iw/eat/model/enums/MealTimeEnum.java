package com.itwray.iw.eat.model.enums;

import lombok.Getter;

/**
 * 用餐时间枚举
 *
 * @author wray
 * @since 2024/5/10
 */
@Getter
public enum MealTimeEnum {
    ANY(0, "任意时间"),
    BREAKFAST(1, "早餐"),
    NOONING(2, "午餐"),
    DINNER(3, "晚餐");

    private final Integer code;

    private final String desc;

    MealTimeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(Integer code) {
        for (MealTimeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}
