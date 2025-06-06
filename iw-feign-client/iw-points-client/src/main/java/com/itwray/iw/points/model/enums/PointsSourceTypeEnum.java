package com.itwray.iw.points.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 积分来源类型枚举
 *
 * @author wray
 * @since 2024/10/1
 */
@Getter
public enum PointsSourceTypeEnum implements ConstantEnum {

    DEFAULT(0, "默认"),
    BOOKKEEPING(1, "记账服务"),
    POINTS_TASK_MANUAL(2, "积分任务-手动触发"),
    POINTS_TASK_TIMING(3, "积分任务-定时触发"),
    FIXED_TASK(4, "常用固定任务"),
    ;

    private final Integer code;

    private final String name;

    PointsSourceTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
