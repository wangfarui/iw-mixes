package com.itwray.iw.auth.model;

/**
 * Redis key 常量
 *
 * @author wray
 * @since 2024/9/12
 */
public abstract class RedisKeyConstants {

    /**
     * 字典缓存信息key
     */
    public static final String DICT_KEY = "dict";

    /**
     * 用户注册时ip次数的key
     */
    public static final String REGISTER_IP_KEY = "register:ip:";
}
