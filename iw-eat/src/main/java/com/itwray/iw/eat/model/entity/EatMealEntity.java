package com.itwray.iw.eat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 用餐表
 *
 * @author wray
 * @since 2024-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("eat_meal")
public class EatMealEntity extends UserEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用餐日期
     */
    private LocalDate mealDate;

    /**
     * 用餐时间(指规定的吃饭时间，通常包括1早餐、2午餐和3晚餐，0表示未规定用餐时间)
     */
    private Integer mealTime;

    /**
     * 用餐人数(0表示不确定用餐人数)
     */
    private Integer diners;

    /**
     * 备注
     */
    private String remark;
}
