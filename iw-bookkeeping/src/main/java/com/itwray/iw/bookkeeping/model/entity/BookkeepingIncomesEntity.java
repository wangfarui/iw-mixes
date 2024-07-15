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
 * 记账-收入表
 *
 * @author wray
 * @since 2024/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_incomes")
public class BookkeepingIncomesEntity extends BaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 收入时间
     */
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
