package com.itwray.iw.points.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 周期性任务的周期间隔枚举
 *
 * @author wray
 * @since 2025/1/13
 */
@Getter
public enum PointsTaskPeriodicIntervalEnum implements BusinessConstantEnum {

    DAILY(1, "每日"),
    WEEKLY(2, "每周"),
    MONTHLY(3, "每月");

    private final Integer code;

    private final String name;

    PointsTaskPeriodicIntervalEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
