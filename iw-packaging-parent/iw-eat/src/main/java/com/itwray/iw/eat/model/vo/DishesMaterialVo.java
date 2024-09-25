package com.itwray.iw.eat.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 菜品用料 VO
 *
 * @author wray
 * @since 2024/5/14
 */
@Data
public class DishesMaterialVo {

    private Integer id;

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
     * 食材价格
     */
    private BigDecimal materialPrice;

    /**
     * 是否需要购买 0否 1是
     */
    private Boolean isPurchase;
}
