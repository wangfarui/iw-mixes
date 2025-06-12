package com.itwray.iw.eat.model;

import com.itwray.iw.starter.redis.RedisKeyManager;
import lombok.Getter;

/**
 * 餐饮服务Redis Key枚举
 *
 * @author wray
 * @since 2025/4/8
 */
@Getter
public enum EatRedisKeyEnum implements RedisKeyManager {

    /**
     * 菜品推荐:[userId]
     */
    DISHES_RECOMMEND("eat:dishes:recommend:%s", 24 * 60 * 60L),
    ;

    private final String pattern;

    private final Long expireTime;

    EatRedisKeyEnum(String pattern, Long expireTime) {
        this.pattern = pattern;
        this.expireTime = expireTime;
    }
}
