package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 记录类型枚举
 *
 * @author wray
 * @since 2024/9/23
 */
@Getter
public enum RecordCategoryEnum implements BusinessConstantEnum {

    CONSUME(1, "支出（消费）"),
    INCOME(2, "收入");

    private final Integer code;

    private final String name;

    RecordCategoryEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
