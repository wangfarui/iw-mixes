package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账(支出/收入)统计排行数据 VO
 *
 * @author wray
 * @since 2024/10/15
 */
@Data
public class BookkeepingStatisticsRankVo {

    private Integer id;

    /**
     * 记录来源
     */
    private String recordSource;

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
