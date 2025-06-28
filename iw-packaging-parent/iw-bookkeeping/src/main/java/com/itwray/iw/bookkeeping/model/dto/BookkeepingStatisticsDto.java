package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.BookkeepingStatisticsTypeEnum;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * 记账统计 DTO
 * <p>适用于支出/收入统计的总统计和排行统计</p>
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingStatisticsDto {

    /**
     * 当前查询的月度
     */
    private LocalDate currentMonth;

    /**
     * 记账记录类型
     */
    private RecordCategoryEnum recordCategory;

    /**
     * 查询时间的开始日期
     */
    private LocalDate currentStartMonth;

    /**
     * 查询时间的结束日期
     */
    private LocalDate currentEndMonth;

    /**
     * 统计类型
     */
    private BookkeepingStatisticsTypeEnum statisticsType;

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
