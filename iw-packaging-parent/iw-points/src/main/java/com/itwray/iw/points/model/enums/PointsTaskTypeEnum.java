package com.itwray.iw.points.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 任务类型枚举
 *
 * @author wray
 * @since 2025/1/13
 */
@Getter
public enum PointsTaskTypeEnum implements BusinessConstantEnum {

    ONCE(1, "一次性任务"),
    PERIODIC(2, "周期性任务");

    private final Integer code;

    private final String name;

    PointsTaskTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
