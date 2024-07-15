package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 记账-支出表
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_expenses")
public class BookkeepingExpensesEntity extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支出时间
     */
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
