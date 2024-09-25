package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 菜品用料表
 *
 * @author wray
 * @since 2024-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_dishes_material")
public class EatDishesMaterialEntity extends IdEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
