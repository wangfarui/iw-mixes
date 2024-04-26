package com.itwray.iw.eat.model.vo;

import lombok.Data;

/**
 * 菜品详情对象
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
public class DishesDetailVo {

    /**
     * id
     */
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
