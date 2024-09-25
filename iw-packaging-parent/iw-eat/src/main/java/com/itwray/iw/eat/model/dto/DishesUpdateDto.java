package com.itwray.iw.eat.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜品修改DTO
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DishesUpdateDto extends DishesAddDto {

    /**
     * 菜品id
     */
    @NotNull(message = "菜品id不能为空")
    private Integer id;
}
