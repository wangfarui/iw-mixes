package com.itwray.iw.auth.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 共享状态枚举
 *
 * @author wray
 * @since 2026/3/12
 */
@Getter
public enum ShareStateEnum implements BusinessConstantEnum {

    NOT_SHARED(0, "不共享"),
    SHARED(1, "共享中"),
    LEFT_GROUP(2, "已离组");

    private final Integer code;

    private final String name;

    ShareStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
