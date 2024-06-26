package com.itwray.iw.eat.model.vo;

import lombok.Data;

/**
 * 菜品制作方法 VO
 *
 * @author wray
 * @since 2024/5/14
 */
@Data
public class DishesCreationMethodVo {

    private Integer id;

    /**
     * 菜品id
     */
    private Integer dishesId;

    /**
     * 制作步骤
     */
    private Integer step;

    /**
     * 步骤图片
     */
    private String stepImage;

    /**
     * 步骤内容
     */
    private String stepContent;
}
