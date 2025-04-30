package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 记账预算类型枚举
 *
 * @author wray
 * @since 2025-04-24
 */
@Getter
public enum BudgetTypeEnum implements BusinessConstantEnum {

    MONTH(1, "月度总预算"),
    MONTH_CATEGORY(11, "月度分类预算"),
    YEAR(2, "年度总预算"),
    YEAR_CATEGORY(21, "年度分类预算"),
    ;

    private final Integer code;

    private final String name;

    BudgetTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否为总预算类型
     */
    public boolean isTotalBudgetType() {
        return this.getCode().equals(MONTH.getCode()) || this.getCode().equals(YEAR.getCode());
    }
}