package com.itwray.iw.eat.model.vo;

import lombok.Data;

/**
 * 用餐菜单详情VO
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public class MealMenuDetailVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 菜品id
     */
    private Integer dishesId;

    /**
     * 菜品名称
     */
    private String dishesName;
}
