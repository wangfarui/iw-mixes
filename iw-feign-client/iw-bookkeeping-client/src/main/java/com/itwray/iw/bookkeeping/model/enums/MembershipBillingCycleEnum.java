package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 会员订阅计费周期枚举
 *
 * @author wray
 * @since 2025/11/5
 */
@Getter
public enum MembershipBillingCycleEnum implements BusinessConstantEnum {

    MONTHLY(1, "按月"),
    YEARLY(2, "按年"),
    WEEKLY(3, "按周"),
    DAILY(4, "按天"),
    ONE_TIME(5, "一次性"),
    CUSTOM(6, "自定义"),
    ;

    private final Integer code;

    private final String name;

    MembershipBillingCycleEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
