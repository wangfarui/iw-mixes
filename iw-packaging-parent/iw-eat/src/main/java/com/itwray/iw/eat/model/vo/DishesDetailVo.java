package com.itwray.iw.eat.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itwray.iw.web.json.serialize.DefaultImageSerializer;
import lombok.Data;

import java.util.List;

/**
 * 菜品详情对象
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
public class DishesDetailVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 菜品名称
     */
    private String dishesName;

    /**
     * 菜品图片
     */
    @JsonSerialize(using = DefaultImageSerializer.class)
    private String dishesImage = "";

    /**
     * 菜品分类(0:无分类, 1:荤, 2:素, 3:荤素)
     */
    private Integer dishesType;

    /**
     * 难度系数(难度依次递增, 0表示未知难度)
     */
    private Integer difficultyFactor;

    /**
     * 用时(分钟, 0表示未知用时)
     */
    private Integer useTime;

    /**
     * 价格(元, 0表示免费)
     */
    private Integer prices;

    /**
     * 状态(1:正常, 2:禁用, 3:售空)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜品用料明细
     */
    private List<DishesMaterialVo> dishesMaterialList;

    /**
     * 菜品制作方法明细
     */
    private List<DishesCreationMethodVo> dishesCreationMethodList;

    /**
     * 菜品所属用户id
     */
    private Integer userId;
}
