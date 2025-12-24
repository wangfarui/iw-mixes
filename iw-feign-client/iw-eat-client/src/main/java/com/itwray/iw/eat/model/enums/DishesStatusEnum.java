package com.itwray.iw.eat.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 菜品状态枚举
 *
 * @author wray
 * @since 2024/5/10
 */
@Getter
public enum DishesStatusEnum implements BusinessConstantEnum {
    NORMAL(1, "正常"),
    DISABLED(2, "禁用"),
    SOLD_OUT(3, "售空");

    private final Integer code;

    private final String name;

    DishesStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
