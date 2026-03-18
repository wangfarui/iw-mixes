package com.itwray.iw.bookkeeping.model.dto;

import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
import com.itwray.iw.web.model.dto.SharedQueryRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * 年度统计查询对象
 *
 * @author wray
 * @since 2025/12/10
 */
@Data
public class BookkeepingRecordsYearStatisticsQueryDto implements SharedQueryRequest {

    private Integer userId;

    /**
     * 统计年份
     */
    private String year;

    /**
     * 是否忽略不计入统计的账单
     * <p>默认为null, 表示查询所有</p>
     * <p>如果为 {@link com.itwray.iw.common.constants.BoolEnum#FALSE} , 表示只查询 is_statistics = 1 的数据</p>
     */
    private Integer ignoreNotStatistics;

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * 查询的记录类型
     */
    private Set<RecordCategoryEnum> recordCategories;

    /**
     * 是否仅查询本人数据
     * <p>默认为null, 表示查询共享口径</p>
     * <p>如果为 {@link com.itwray.iw.common.constants.BoolEnum#TRUE} , 表示仅查询本人数据</p>
     */
    private Integer queryOnlyMyself;
}
