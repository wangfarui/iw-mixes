package com.itwray.iw.eat.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.json.deserialize.IntegerToZeroDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
     * 菜品分类(0:无分类, 1:荤, 2:素, 3:荤素)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    private Integer dishesType;

    /**
     * 难度系数(难度依次递增, 0表示未知难度)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
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
}
