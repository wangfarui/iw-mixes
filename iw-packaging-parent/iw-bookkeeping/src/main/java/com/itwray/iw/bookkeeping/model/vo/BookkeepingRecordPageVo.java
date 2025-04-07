package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwray.iw.bookkeeping.model.enums.RecordCategoryEnum;
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
     * 记录时间字符串
     */
    private String recordTimeStr;

    /**
     * 记录类型
     */
    private RecordCategoryEnum recordCategory;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 记录分类
     */
    private Integer recordType;
}
