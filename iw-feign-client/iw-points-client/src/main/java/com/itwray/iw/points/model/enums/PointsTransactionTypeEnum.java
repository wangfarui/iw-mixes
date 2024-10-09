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

    /**
     * 根据积分分数的正负数决定积分变动类型code
     *
     * @param points 积分分数
     * @return {@link PointsTransactionTypeEnum#getCode()}
     */
    public static Integer getCodeByPoints(Integer points) {
        if (points == null || points >= 0) {
            return INCREASE.getCode();
        } else {
            return DEDUCT.getCode();
        }
    }
}
