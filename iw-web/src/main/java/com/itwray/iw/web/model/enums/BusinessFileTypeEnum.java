package com.itwray.iw.web.model.enums;

import lombok.Getter;

/**
 * 业务文件类型
 *
 * @author wray
 * @since 2025/4/23
 */
@Getter
public enum BusinessFileTypeEnum implements BusinessConstantEnum {

    BOOKKEEPING_RECORDS(1, "记账记录附件"),
    ;

    private final Integer code;

    private final String name;

    BusinessFileTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
