package com.itwray.iw.bookkeeping.model.vo;

import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.web.model.vo.DetailVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 记账预算表 详情VO
 *
 * @author wray
 * @since 2025-04-24
 */
@Data
@Schema(name = "记账预算表 详情VO")
public class BookkeepingBudgetDetailVo implements DetailVo {

    @Schema(title = "id")
    private Integer id;

    @Schema(title = "预算类型")
    private BudgetTypeEnum budgetType;

    @Schema(title = "记录分类")
    private Integer recordType;

    @Schema(title = "预算金额")
    private BigDecimal budgetAmount;

}
