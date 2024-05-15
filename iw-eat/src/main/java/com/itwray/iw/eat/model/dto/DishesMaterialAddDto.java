package com.itwray.iw.eat.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 菜品用料 DTO
 *
 * @author wray
 * @since 2024/5/14
 */
@Data
public class DishesMaterialAddDto {

    /**
     * 食材名称
     */
    @NotBlank(message = "食材名称不能为空")
    private String materialName;

    /**
     * 食材用量
     */
    private String materialDosage;

    /**
     * 是否需要购买 0否 1是
     */
    private Boolean isPurchase;
}
