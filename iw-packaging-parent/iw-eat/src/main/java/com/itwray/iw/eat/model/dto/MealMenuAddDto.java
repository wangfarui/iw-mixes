package com.itwray.iw.eat.model.dto;

import lombok.Data;

/**
 * 用餐菜单新增DTO
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public class MealMenuAddDto {

    /**
     * 菜品id
     */
    private Integer dishesId;

    /**
     * 菜品名称
     */
    private String dishesName;
}
