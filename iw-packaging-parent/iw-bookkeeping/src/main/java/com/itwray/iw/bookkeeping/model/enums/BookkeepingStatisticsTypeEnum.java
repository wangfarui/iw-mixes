package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 记账统计类型枚举
 *
 * @author farui.wang
 * @since 2025/5/27
 */
@Getter
public enum BookkeepingStatisticsTypeEnum implements BusinessConstantEnum {

    MONTH(1, "月度统计"),
    YEAR(2, "年度统计");

    private final Integer code;

    private final String name;

    BookkeepingStatisticsTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
