package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 记账行为表
 *
 * @author wray
 * @since 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_actions")
public class BookkeepingActionsEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 记录类型(1:支出, 2:收入)
     */
    private Integer recordCategory;

    /**
     * 记录来源
     */
    private String recordSource;

    /**
     * 记录分类
     */
    private Integer recordType;

    /**
     * 记录图标
     */
    private String recordIcon;

    /**
     * 记录标签(标签字典id逗号拼接)
     */
    private String recordTags;

    /**
     * 排序 0-默认排序
     */
    private BigDecimal sort;
}
