package com.itwray.iw.eat.model.vo;

import com.itwray.iw.eat.model.enums.MealTimeEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 用餐详情VO
 *
 * @author wray
 * @since 2024/4/24
 */
@Data
public class MealDetailVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 用餐日期
     */
    private LocalDate mealDate;

    /**
     * 用餐时间(指规定的吃饭时间，通常包括1早餐、2午餐和3晚餐，0表示未规定用餐时间)
     */
    private MealTimeEnum mealTime;

    /**
     * 用餐时间描述
     */
    private String mealTimeDesc;

    /**
     * 用餐人数(0表示不确定用餐人数)
     */
    private Integer diners;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用餐菜单列表
     */
    private List<MealMenuDetailVo> mealMenuList;
}
