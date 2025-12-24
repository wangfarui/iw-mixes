package com.itwray.iw.web.model.enums;

import lombok.Getter;

/**
 * 排序方式枚举
 *
 * @author wray
 * @since 2025/11/12
 */
@Getter
public enum SortWayEnum implements BusinessConstantEnum {

    ASC(1, "asc", "升序"),
    DESC(2, "desc", "降序");

    private final Integer code;

    private final String name;

    private final String desc;

    SortWayEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    /**
     * 是否为升序
     * <p>为null时，默认返回false，表达默认降序排序</p>
     */
    public static boolean isAsc(SortWayEnum sortWayEnum) {
        return ASC.equals(sortWayEnum);
    }
}
