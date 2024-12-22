package com.itwray.iw.starter.redis;

/**
 * Redis Key 管理器
 *
 * @author wray
 * @since 2024/12/22
 */
public interface RedisKeyManager {

    String getPattern();

    default String getKey(Object... args) {
        return String.format(getPattern(), args);
    }
}
