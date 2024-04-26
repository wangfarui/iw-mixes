package com.itwray.iw.eat.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.model.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜品分页查询对象
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DishesPageDto extends PageDto {

    /**
     * 菜品名称
     */
    private String dishesName;

    /**
     * 菜品分类(0:无分类, 1:荤, 2:素, 3:荤素)
     */
    private Integer dishesType;

    /**
     * 状态(1:正常, 2:禁用, 3:售空)
     */
    private Integer status;
}
