package com.itwray.iw.eat.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.json.deserialize.IntegerToZeroDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 菜品新增DTO
 *
 * @author wray
 * @since 2024/4/26
 */
@Data
public class DishesAddDto {

    /**
     * 菜品名称
     */
    @NotBlank(message = "菜品名称不能为空")
    private String dishesName;

    /**
     * 菜品图片
     */
    private String dishesImage;

    /**
     * 菜品分类(0:无分类, 1:荤, 2:素, 3:荤素)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    private Integer dishesType;

    /**
     * 难度系数(难度依次递增, 0表示未知难度)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    @Max(value = 8, message = "难度系数不能超过8")
    private Integer difficultyFactor;

    /**
     * 用时(分钟, 0表示未知用时)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    private Integer useTime;

    /**
     * 价格(元, 0表示免费)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    private Integer prices;

    /**
     * 备注
     */
    @Size(max = 255, message = "备注不能超过255个字符")
    private String remark;

    /**
     * 菜品用料对象集合
     */
    private List<DishesMaterialAddDto> dishesMaterialList;

    /**
     * 菜品制作方法步骤集合
     */
    private List<DishesCreationMethodAddDto> dishesCreationMethodList;
}
