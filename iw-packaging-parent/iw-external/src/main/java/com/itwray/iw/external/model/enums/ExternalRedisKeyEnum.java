package com.itwray.iw.external.model.enums;

import com.itwray.iw.starter.redis.RedisKeyManager;
import lombok.Getter;

/**
 * 外部服务的 Redis key 枚举
 *
 * @author wray
 * @since 2025/2/21
 */
@Getter
public enum ExternalRedisKeyEnum implements RedisKeyManager {

    /**
     * 客户端ip地址:[ip]
     */
    IP_ADDRESS_KEY("external:ip:%s", 60 * 60 * 24 * 7L),
    /**
     * 城市天气:[城市code]
     */
    CITY_WEATHER_KEY("external:city:%s", 60 * 60 * 3L),
    /**
     * 站点监测
     */
    SITE_MONITORS_KEY("external:site-monitors", 60 * 10L),
    /**
     * 每日热点:[热点渠道code]
     */
    DAILY_HOT_KEY("external:daily-hot:%s", 60 * 30L),
    /**
     * AI对话内容排序
     */
    AI_CHAT_SORT("external:ai:chat:sort:%s", 60 * 60 * 24L),
    /**
     * AI对话内容
     */
    AI_CHAT_CONTENT("external:ai:chat:content:%s", 60 * 60 * 24L),
    ;

    private final String pattern;

    private final Long expireTime;

    ExternalRedisKeyEnum(String pattern, Long expireTime) {
        this.pattern = pattern;
        this.expireTime = expireTime;
    }
}
