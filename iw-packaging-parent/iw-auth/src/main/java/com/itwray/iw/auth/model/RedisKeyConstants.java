package com.itwray.iw.auth.model;

/**
 * Redis key 常量
 *
 * @author wray
 * @since 2024/9/12
 */
public abstract class RedisKeyConstants {

    /**
     * 字典缓存信息key:[userId]
     */
    public static final String DICT_KEY = "dict:";

    /**
     * 指定ip的用户注册次数:[ipAddress]
     */
    public static final String REGISTER_IP_KEY = "register:ip:";

    /**
     * 电话号码的授权验证码:[phoneNumber]
     */
    public static final String PHONE_VERIFY_KEY = "phone:verify:";

    /**
     * 电话号码获取验证码时，指定ip获取验证码的次数:[ipAddress]
     */
    public static final String PHONE_VERIFY_IP_KEY = "phone:verify:ip:";
}
