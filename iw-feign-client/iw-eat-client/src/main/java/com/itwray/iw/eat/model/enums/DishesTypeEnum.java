package com.itwray.iw.eat.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 菜品分类枚举
 *
 * @author wray
 * @since 2024/5/10
 */
@Getter
public enum DishesTypeEnum implements BusinessConstantEnum {
    MEAL_DIET(1, "荤菜"),
    VEGETABLE_DISHES(2, "素菜"),
    MIXTURE(3, "荤素搭配");

    private final Integer code;

    private final String name;

    DishesTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
