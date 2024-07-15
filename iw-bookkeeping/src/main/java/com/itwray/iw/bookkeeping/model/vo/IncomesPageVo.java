package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账收入 分页VO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class IncomesPageVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 收入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime incomeDate;

    /**
     * 收入来源
     */
    private String incomeSource;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount;

    /**
     * 备注
     */
    private String remark;
}
