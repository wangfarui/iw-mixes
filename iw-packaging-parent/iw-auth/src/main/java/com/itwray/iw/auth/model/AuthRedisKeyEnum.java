package com.itwray.iw.auth.model;

import com.itwray.iw.starter.redis.RedisKeyManager;
import lombok.Getter;

/**
 * Auth服务的 Redis key 枚举
 *
 * @author wray
 * @since 2024/9/12
 */
@Getter
public enum AuthRedisKeyEnum implements RedisKeyManager {

    /**
     * 用户token key:[token]
     * <p>用户:token = 1:n</p>
     * <p>token有效期 {@link com.itwray.iw.auth.dao.AuthUserDao#TOKEN_ACTIVE_TIME}</p>
     * <p>该缓存有效期会在每次网关调用时刷新</p>
     */
    USER_TOKEN_KEY("auth:token:%s"),

    /**
     * 用户token集合key:[userId]
     * <p>用户已登录的所有token</p>
     * <p>token有效期 {@link com.itwray.iw.auth.dao.AuthUserDao#TOKEN_ACTIVE_TIME}</p>
     * <p>该缓存有效期会在每次登录和每次网关调用时刷新</p>
     */
    USER_TOKEN_SET_KEY("auth:token:set:%s"),

    /**
     * 字典缓存信息key:[userId]
     */
    DICT_KEY("auth:dict:%s"),

    /**
     * 指定ip的用户注册次数:[ipAddress]
     * <p>防止用户恶意注册</p>
     */
    REGISTER_IP_KEY("auth:register:ip:%s"),

    /**
     * 电话号码的授权验证码:[phoneNumber]
     */
    PHONE_VERIFY_KEY("auth:phone:verify:%s"),

    /**
     * 电话号码获取验证码时，指定ip获取验证码的次数:[ipAddress]
     */
    PHONE_VERIFY_IP_KEY("auth:phone:verify:ip:%s"),

    /**
     * 登录失败-用户+客户端ip:[account]:[ipAddress]
     */
    LOGIN_ACTION_USER_IP_KEY("auth:login:fail:user:%s:%s"),

    /**
     * 登录失败-客户端ip:[ipAddress]
     */
    LOGIN_FAIL_IP_KEY("auth:login:fail:ip:%s");

    private final String pattern;

    AuthRedisKeyEnum(String pattern) {
        this.pattern = pattern;
    }
}