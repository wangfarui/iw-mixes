package com.itwray.iw.points.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 任务计划周期 枚举
 *
 * @author wray
 * @since 2025/3/19
 */
@Getter
public enum TaskPlanCycleEnum implements BusinessConstantEnum {

    DAY(1, "每日"),
    WEEK(2, "每周"),
    MONTH(3, "每月"),
    YEAR(4, "每年"),
    CUSTOM(5, "自定义"),
    ;

    private final Integer code;

    private final String name;

    TaskPlanCycleEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}