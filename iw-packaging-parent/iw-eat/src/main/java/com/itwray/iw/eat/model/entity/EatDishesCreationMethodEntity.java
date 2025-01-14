package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜品制作方法表
 *
 * @author wray
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_dishes_creation_method")
public class EatDishesCreationMethodEntity extends IdEntity<Integer> {

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
