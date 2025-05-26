package com.itwray.iw.bookkeeping.model.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;

/**
 * 钱包记录变动类型 枚举
 *
 * @author farui.wang
 * @since 2025/5/26
 */
@Getter
public enum WalletRecordsChangeTypeEnum implements BusinessConstantEnum {

    BALANCE(1, "余额"),
    ASSETS(2, "资产"),
    ;

    private final Integer code;

    private final String name;

    WalletRecordsChangeTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
