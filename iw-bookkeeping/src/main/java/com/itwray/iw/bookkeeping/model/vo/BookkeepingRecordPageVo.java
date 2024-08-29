package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账记录 分页VO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class BookkeepingRecordPageVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "MM-dd HH:mm")
    private LocalDateTime recordTime;

    /**
     * 记录类型
     */
    private Integer recordCategory;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 金额
     */
    private BigDecimal amount;
}
