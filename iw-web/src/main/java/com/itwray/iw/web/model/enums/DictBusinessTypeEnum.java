package com.itwray.iw.web.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 字典业务类型枚举
 * <p>一般属于在服务端硬编码获取code值，不与客户端交互</p>
 *
 * @author wray
 * @since 2024/12/19
 */
@Getter
public enum DictBusinessTypeEnum implements ConstantEnum {
    BOOKKEEPING_RECORD_TAG(4001, "记账记录标签");

    private final Integer code;

    private final String name;

    DictBusinessTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
