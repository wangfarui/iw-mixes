package com.itwray.iw.eat.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用餐编辑DTO
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MealUpdateDto extends MealAddDto {

    /**
     * 用餐id
     */
    @NotNull(message = "用餐id不能为空")
    private Integer id;
}
