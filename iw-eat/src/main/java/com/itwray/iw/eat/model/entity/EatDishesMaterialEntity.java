package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 菜品用料表
 *
 * @author wray
 * @since 2024-04-23
 */
@Data
@TableName("eat_dishes_material")
public class EatDishesMaterialEntity {

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
}
