package com.itwray.iw.points.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 任务状态 枚举
 *
 * @author wray
 * @since 2025/3/19
 */
@Getter
public enum TaskStatusEnum implements BusinessConstantEnum {

    WAIT(0, "待完成"),
    DONE(1, "已完成"),
    GIVE_UP(2, "已放弃"),
    DELETED(3, "已删除"),
    ;

    private final Integer code;

    private final String name;

    TaskStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}