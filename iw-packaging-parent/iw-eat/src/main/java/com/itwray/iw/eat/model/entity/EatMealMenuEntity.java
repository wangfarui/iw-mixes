package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用餐菜单表
 *
 * @author wray
 * @since 2024-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_meal_menu")
public class EatMealMenuEntity extends IdEntity<Integer> {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用餐id
     */
    private Integer mealId;

    /**
     * 菜品id
     */
    private Integer dishesId;

    /**
     * 菜品名称
     */
    private String dishesName;
}
