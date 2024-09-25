package com.itwray.iw.eat.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 菜品制作方法 DTO
 *
 * @author wray
 * @since 2024/5/14
 */
@Data
public class DishesCreationMethodAddDto {

    /**
     * 步骤图片
     */
    private String stepImage;

    /**
     * 步骤内容
     */
    @NotBlank(message = "步骤内容不能为空")
    private String stepContent;
}
