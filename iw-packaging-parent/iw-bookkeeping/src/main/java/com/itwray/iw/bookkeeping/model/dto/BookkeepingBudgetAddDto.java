package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.web.model.dto.AddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 记账预算表 新增DTO
 *
 * @author wray
 * @since 2025-04-24
 */
@Data
@Schema(name = "记账预算表 新增DTO")
public class BookkeepingBudgetAddDto implements AddDto {

    @Schema(title = "预算类型")
    @NotNull(message = "预算类型不能为空")
    private BudgetTypeEnum budgetType;

    @Schema(title = "记录分类")
    private Integer recordType;

    @Schema(title = "预算金额")
    @NotNull(message = "预算金额不能为空")
    @Min(value = 0, message = "预算金额不能小于0")
    private BigDecimal budgetAmount;

    @Schema(title = "预算月份")
    private LocalDate budgetMonth;

    @Schema(title = "预算年份")
    private Integer budgetYear;
}
