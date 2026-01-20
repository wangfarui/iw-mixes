package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 冰箱食材表
 *
 * @author wray
 * @since 2026-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_fridge_food")
public class EatFridgeFoodEntity extends UserEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 食材名称
     */
    private String name;

    /**
     * 食材图标
     */
    private String emoji;

    /**
     * 食材分类
     */
    private Integer category;

    /**
     * 食材分区
     */
    private Integer section;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 入库日期
     */
    private LocalDate addDate;

    /**
     * 过期日期
     */
    private LocalDate expireDate;
}
