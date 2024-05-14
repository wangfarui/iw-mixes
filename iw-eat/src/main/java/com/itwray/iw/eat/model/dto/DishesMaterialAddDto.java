package com.itwray.iw.eat.model.dto;

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
     * 菜品id
     */
    private Integer dishesId;

    /**
     * 食材名称
     */
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
