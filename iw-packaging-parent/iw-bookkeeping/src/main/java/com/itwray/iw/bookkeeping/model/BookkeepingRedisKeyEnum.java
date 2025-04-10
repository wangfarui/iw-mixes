package com.itwray.iw.bookkeeping.model;

import com.itwray.iw.starter.redis.RedisKeyManager;
import lombok.Getter;

/**
 * 记账服务Redis Key枚举
 *
 * @author wray
 * @since 2025/4/8
 */
@Getter
public enum BookkeepingRedisKeyEnum implements RedisKeyManager {

    /**
     * 记账行为列表数据缓存:[userId]:[recordCategory]
     */
    ACTION_LIST("bookkeeping:actionList:%s:%s", 7 * 24 * 60 * 60L),
    ;

    private final String pattern;

    private final Long expireTime;

    BookkeepingRedisKeyEnum(String pattern, Long expireTime) {
        this.pattern = pattern;
        this.expireTime = expireTime;
    }
}
