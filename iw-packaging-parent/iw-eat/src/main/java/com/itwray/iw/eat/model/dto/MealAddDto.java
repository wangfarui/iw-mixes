package com.itwray.iw.eat.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itwray.iw.web.json.deserialize.IntegerToZeroDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 用餐新增DTO
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public class MealAddDto {

    /**
     * 用餐日期
     */
    @NotNull(message = "用餐日期不能为空")
    private LocalDate mealDate;

    /**
     * 用餐时间(指规定的吃饭时间，通常包括1早餐、2午餐和3晚餐，0表示未规定用餐时间)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    private Integer mealTime;

    /**
     * 用餐人数(0表示不确定用餐人数)
     */
    @JsonDeserialize(using = IntegerToZeroDeserializer.class)
    private Integer diners;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单明细
     */
    private List<MealMenuAddDto> mealMenuList;
}
