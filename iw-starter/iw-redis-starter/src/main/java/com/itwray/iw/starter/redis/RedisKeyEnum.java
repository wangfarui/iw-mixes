package com.itwray.iw.starter.redis;

import lombok.Getter;

/**
 * Redis Key 公共枚举
 *
 * @author wray
 * @since 2024/12/20
 */
@Getter
public enum RedisKeyEnum implements RedisKeyManager {

    /**
     * 服务内部调用时Feign密钥的key
     * <p>一个密钥只会被使用一次</p>
     */
    FEIGN_SECRET_KEY("common:feign:%s");

    private final String pattern;

    RedisKeyEnum(String pattern) {
        this.pattern = pattern;
    }
}
