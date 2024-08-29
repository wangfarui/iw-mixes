package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 记账-记录表
 *
 * @author wray
 * @since 2024/8/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_records")
public class BookkeepingRecordsEntity extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 记录日期
     */
    private LocalDate recordDate;

    /**
     * 记录时间
     */
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

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 备注
     */
    private String remark;
}
