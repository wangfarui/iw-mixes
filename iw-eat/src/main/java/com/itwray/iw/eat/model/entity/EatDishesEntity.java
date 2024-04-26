package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 菜品表
 *
 * @author wray
 * @since 2024-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_dishes")
public class EatDishesEntity extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜品名称
     */
    private String dishesName;

    /**
     * 菜品分类(0:无分类, 1:荤, 2:素, 3:荤素)
     */
    private Integer dishesType;

    /**
     * 难度系数(难度依次递增, 0表示未知难度)
     */
    private Integer difficultyFactor;

    /**
     * 用时(分钟, 0表示未知用时)
     */
    private Integer useTime;

    /**
     * 价格(元, 0表示免费)
     */
    private Integer prices;

    /**
     * 状态(1:正常, 2:禁用, 3:售空)
     */
    private Integer status;
}
