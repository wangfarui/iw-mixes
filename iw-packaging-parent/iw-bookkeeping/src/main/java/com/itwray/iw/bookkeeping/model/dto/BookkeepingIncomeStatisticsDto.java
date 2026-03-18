package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.BookkeepingStatisticsTypeEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.dto.SharedQueryRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 记账收入统计 DTO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingIncomeStatisticsDto implements SharedQueryRequest {

    /**
     * 当前查询的月度
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate currentMonth;

    /**
     * 统计类型
     */
    @NotNull(message = "统计类型不能为空")
    private BookkeepingStatisticsTypeEnum statisticsType;

    /**
     * 排行统计数量
     */
    private Integer limit = 10;

    /**
     * 是否仅查询本人数据
     * <p>默认为null, 表示查询共享口径</p>
     * <p>如果为 {@link com.itwray.iw.common.constants.BoolEnum#TRUE} , 表示仅查询本人数据</p>
     */
    private Integer queryOnlyMyself;

}
