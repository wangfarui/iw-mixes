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
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate currentMonth;

    /**
     * 记账记录类型
     */
    private RecordCategoryEnum recordCategory;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate currentStartMonth;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate currentEndMonth;

    /**
     * 排行统计数量
     */
    private Integer limit = 10;

    /**
     * 是否查询全部账单
     * <p>默认为null, 表示查询所有</p>
     * <p>如果为 {@link com.itwray.iw.common.constants.BoolEnum#FALSE} , 表示只查询 is_statistics = 1 的数据</p>
     */
    private Integer isSearchAll;
}
