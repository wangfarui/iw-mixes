package com.itwray.iw.external.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itwray.iw.external.config.BlogAccessProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 博客访问密码失败次数限流器。
 *
 * @author wray
 * @since 2026/6/24
 */
@Component
public class BlogAccessRateLimiter {

    private final BlogAccessProperties properties;

    private final Cache<String, AtomicInteger> failedAttemptCache;

    public BlogAccessRateLimiter(BlogAccessProperties properties) {
        this.properties = properties;
        this.failedAttemptCache = Caffeine.newBuilder()
                .expireAfterWrite(properties.getRateLimit().getSafeWindowSeconds(), TimeUnit.SECONDS)
                .maximumSize(10000)
                .build();
    }

    public boolean isBlocked(String clientIp, String scope) {
        AtomicInteger failedAttempts = failedAttemptCache.getIfPresent(buildKey(clientIp, scope));
        return failedAttempts != null
                && failedAttempts.get() >= properties.getRateLimit().getSafeMaxFailures();
    }

    public void recordFailure(String clientIp, String scope) {
        failedAttemptCache.asMap().compute(buildKey(clientIp, scope), (key, failedAttempts) -> {
            if (failedAttempts == null) {
                return new AtomicInteger(1);
            }
            failedAttempts.incrementAndGet();
            return failedAttempts;
        });
    }

    public void clear(String clientIp, String scope) {
        failedAttemptCache.invalidate(buildKey(clientIp, scope));
    }

    private String buildKey(String clientIp, String scope) {
        String safeIp = StringUtils.defaultIfBlank(clientIp, "0.0.0.0");
        String safeScope = StringUtils.defaultIfBlank(scope, "unknown").toLowerCase(Locale.ROOT);
        return safeIp + ":" + safeScope;
    }
}
