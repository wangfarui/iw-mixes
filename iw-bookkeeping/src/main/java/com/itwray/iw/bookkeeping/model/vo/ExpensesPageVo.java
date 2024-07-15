package com.itwray.iw.bookkeeping.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账支出 分页VO
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
public class ExpensesPageVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 支出时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expenseDate;

    /**
     * 支出项目
     */
    private String expenseItem;

    /**
     * 支出金额
     */
    private BigDecimal expenseAmount;

    /**
     * 备注
     */
    private String remark;
}
