package com.itwray.iw.points.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 积分变动类型枚举
 *
 * @author wray
 * @since 2024/9/23
 */
@Getter
public enum PointsTransactionTypeEnum implements ConstantEnum {

    INCREASE(1, "增加积分"),
    DEDUCT(2, "扣减积分");

    private final Integer code;

    private final String name;

    PointsTransactionTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
