package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.common.utils.DateUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 记账支出月度统计 DTO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingConsumeMonthStatisticsDto {

    /**
     * 当前查询的月度
     */
    @NotNull(message = "选择月份不能为空")
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate currentMonth;

    /**
     * 记账记录类型
     */
    private RecordCategoryEnum recordCategory;

    private LocalDate currentStartMonth;

    private LocalDate currentEndMonth;

    /**
     * 排行统计数量
     */
    private Integer limit = 10;
}
