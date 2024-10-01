package com.itwray.iw.points.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 积分来源类型枚举
 *
 * @author wray
 * @since 2024/10/1
 */
@Getter
public enum PointsSourceTypeEnum implements ConstantEnum {

    DEFAULT(0, "默认"),
    BOOKKEEPING(1, "记账服务");

    private final Integer code;

    private final String name;

    PointsSourceTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
