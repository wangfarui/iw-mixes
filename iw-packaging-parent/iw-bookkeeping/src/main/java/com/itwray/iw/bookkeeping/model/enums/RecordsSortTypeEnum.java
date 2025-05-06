package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 记账记录排序类型枚举
 *
 * @author farui.wang
 * @since 2025/5/6
 */
@Getter
public enum RecordsSortTypeEnum implements BusinessConstantEnum {

    DEFAULT(0, "id", "默认"),
    RECORD_TIME(1, "record_time", "记账时间"),
    RECORD_AMOUNT(2, "amount", "记账金额");

    private final Integer code;

    private final String name;

    private final String desc;

    RecordsSortTypeEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }
}
