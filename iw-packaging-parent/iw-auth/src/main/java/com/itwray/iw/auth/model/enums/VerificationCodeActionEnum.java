package com.itwray.iw.auth.model.enums;

import com.itwray.iw.common.ConstantEnum;
import lombok.Getter;

/**
 * 验证码操作行为枚举
 *
 * @author wray
 * @since 2024/12/18
 */
@Getter
public enum VerificationCodeActionEnum implements ConstantEnum {

    EDIT_PASSWORD(1, "修改密码"),
    OTHER(0, "其他"),
    APPLICATION_ACCOUNT_REFRESH_PASSWORD(2, "应用账号刷新密码操作"),
    ;

    private final Integer code;

    private final String name;

    VerificationCodeActionEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static VerificationCodeActionEnum of(Integer action) {
        if (action == null) {
            return VerificationCodeActionEnum.OTHER;
        }
        for (VerificationCodeActionEnum actionEnum : VerificationCodeActionEnum.values()) {
            if (actionEnum.getCode().equals(action)) {
                return actionEnum;
            }
        }
        return VerificationCodeActionEnum.OTHER;
    }
}
