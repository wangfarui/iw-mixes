package com.itwray.iw.bookkeeping.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.RecordsSortTypeEnum;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.dto.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 记账记录 分页DTO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingRecordPageDto extends PageDto {

    /**
     * 记账记录开始时间
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordStartDate;

    /**
     * 记账记录结束时间
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordEndDate;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 记录类型
     */
    private Integer recordCategory;

    /**
     * 是否查询全部账单
     * <p>默认为null, 表示查询所有</p>
     * <p>如果为 {@link com.itwray.iw.common.constants.BoolEnum#FALSE} , 表示只查询 is_statistics = 1 的数据</p>
     */
    private Integer isSearchAll;

    /**
     * 最小金额
     */
    private BigDecimal mixAmount;

    /**
     * 最大金额
     */
    private BigDecimal maxAmount;

    /**
     * 记账标签id集合
     */
    private List<Integer> tagIdList;

    /**
     * 标签业务类型
     */
    private Integer tagBusinessType;

    /**
     * 记录排序类型
     */
    private RecordsSortTypeEnum sortType;

    /**
     * 排序方式 1=升序, 其他值=降序
     */
    private Integer sortWay;
}
