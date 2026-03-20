package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.common.utils.DateUtils;
import com.itwray.iw.web.model.vo.AbstractUserOwnerVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 记账(支出/收入)统计排行数据 VO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookkeepingStatisticsRankVo extends AbstractUserOwnerVo {

    private Integer id;

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 记录日期
     */
    @JsonFormat(pattern = DateUtils.DATE_FORMAT)
    private LocalDate recordDate;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "MM-dd HH:mm")
    private LocalDateTime recordTime;

    /**
     * 金额
     */
    private BigDecimal amount;
}
