package com.itwray.iw.bookkeeping.model.vo;

import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账预算统计VO
 *
 * @author wray
 * @since 2025/4/24
 */
@Data
public class BookkeepingBudgetStatisticsVo {

    @Schema(title = "预算id")
    private Integer id;

    @Schema(title = "预算类型")
    private BudgetTypeEnum budgetType;

    @Schema(title = "记录分类")
    private Integer recordType;

    @Schema(title = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(title = "支出金额")
    private BigDecimal usedAmount;

    @Schema(title = "剩余金额")
    private BigDecimal remainingAmount;

    @Schema(title = "支出占比")
    private BigDecimal usedRatio;
}
