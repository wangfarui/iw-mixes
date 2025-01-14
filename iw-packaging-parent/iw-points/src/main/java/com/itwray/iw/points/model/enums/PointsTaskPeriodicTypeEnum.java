package com.itwray.iw.points.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 周期性任务的类型枚举
 *
 * @author wray
 * @since 2025/1/13
 */
@Getter
public enum PointsTaskPeriodicTypeEnum implements BusinessConstantEnum {

    REMIND(1, "提醒任务"),
    CLOCK_IN(2, "打卡任务");

    private final Integer code;

    private final String name;

    PointsTaskPeriodicTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
