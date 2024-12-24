package com.itwray.iw.external.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * SMS短信记录发送状态枚举
 *
 * @author wray
 * @since 2024/12/24
 */
@Getter
public enum SmsSendStatusEnum implements ConstantEnum {

    WAITING(0, "待发送"),
    SUCCESS(1, "发送成功"),
    FAIL(2, "发送失败");

    private final Integer code;

    private final String name;

    SmsSendStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
