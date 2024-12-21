package com.itwray.iw.starter.redis;

/**
 * Redis通用常量Key
 *
 * @author wray
 * @since 2024/12/20
 */
public abstract class RedisKeyConstants {

    /**
     * 服务内部调用时Feign密钥的key
     * <p>一个密钥只会被使用一次</p>
     */
    public static final String FEIGN_SECRET_KEY = "feign:";
}
