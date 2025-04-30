package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.bookkeeping.model.enums.BudgetTypeEnum;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 记账预算表
 *
 * @author wray
 * @since 2025-04-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_budget")
public class BookkeepingBudgetEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预算类型
     */
    private BudgetTypeEnum budgetType;

    /**
     * 记录分类
     * <p>对应 {@link BookkeepingRecordsEntity#getRecordType()} 字段</p>
     */
    private Integer recordType;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;
}
