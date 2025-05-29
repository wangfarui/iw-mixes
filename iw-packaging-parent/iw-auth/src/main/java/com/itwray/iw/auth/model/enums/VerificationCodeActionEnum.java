package com.itwray.iw.auth.model.enums;

import com.itwray.iw.auth.model.AuthRedisKeyEnum;
import com.itwray.iw.common.ConstantEnum;
import com.itwray.iw.starter.redis.RedisKeyManager;
import lombok.Getter;

/**
 * 验证码操作行为枚举
 *
 * @author wray
 * @since 2024/12/18
 */
@Getter
public enum VerificationCodeActionEnum implements ConstantEnum {

    EDIT_PASSWORD(1, "修改密码", AuthRedisKeyEnum.EDIT_PASSWORD_KEY),
    APPLICATION_ACCOUNT_REFRESH_PASSWORD(2, "应用账号刷新密码操作", AuthRedisKeyEnum.APPLICATION_ACCOUNT_REFRESH_KEY),
    USER_LOGIN_REGISTER(3, "用户登录/注册", AuthRedisKeyEnum.USER_LOGIN_PHONE_VERIFY_KEY),
    ;

    private final Integer code;

    private final String name;

    private final RedisKeyManager keyManager;

    VerificationCodeActionEnum(Integer code, String name, RedisKeyManager keyManager) {
        this.code = code;
        this.name = name;
        this.keyManager = keyManager;
    }
}
