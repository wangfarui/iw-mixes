package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 会员订阅周期单位枚举
 *
 * @author wray
 * @since 2025/11/5
 */
@Getter
public enum MembershipCycleUnitEnum implements BusinessConstantEnum {

    DAY(1, "天"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    YEAR(4, "年"),
    ;

    private final Integer code;

    private final String name;

    MembershipCycleUnitEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
