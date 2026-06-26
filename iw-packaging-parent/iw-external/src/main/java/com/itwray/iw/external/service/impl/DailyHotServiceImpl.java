package com.itwray.iw.external.service.impl;

import com.itwray.iw.external.config.DailyHotProperties;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotResult;
import com.itwray.iw.external.model.enums.DailyHotSourceEnum;
import com.itwray.iw.external.model.enums.ExternalRedisKeyEnum;
import com.itwray.iw.external.service.DailyHotService;
import com.itwray.iw.external.service.dailyhot.DailyHotProvider;
import com.itwray.iw.external.service.dailyhot.DailyHotProviderRegistry;
import com.itwray.iw.external.service.dailyhot.impl.BuiltInDailyHotProvider;
import com.itwray.iw.starter.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 每日热点服务实现。
 *
 * @author wray
 * @since 2026/6/26
 */
@Slf4j
@Service
public class DailyHotServiceImpl implements DailyHotService {

    private final DailyHotProviderRegistry dailyHotProviderRegistry;

    private final DailyHotProperties dailyHotProperties;

    private final BuiltInDailyHotProvider builtInDailyHotProvider;

    public DailyHotServiceImpl(DailyHotProviderRegistry dailyHotProviderRegistry,
                               DailyHotProperties dailyHotProperties,
                               BuiltInDailyHotProvider builtInDailyHotProvider) {
        this.dailyHotProviderRegistry = dailyHotProviderRegistry;
        this.dailyHotProperties = dailyHotProperties;
        this.builtInDailyHotProvider = builtInDailyHotProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Object> getDailyHot(String source) {
        String normalizedSource = normalizeSource(source);
        if (StringUtils.isBlank(normalizedSource)) {
            return DailyHotResult.failure(source, "无效的热点来源").toResponseMap();
        }
        DailyHotSourceEnum sourceEnum = DailyHotSourceEnum.of(normalizedSource);
        DailyHotProvider provider = dailyHotProviderRegistry.getProvider(normalizedSource);
        if (provider == null && !builtInDailyHotProvider.supports(sourceEnum)) {
            return DailyHotResult.failure(normalizedSource, "暂不支持的热点来源").toResponseMap();
        }
        if (!dailyHotProperties.isSourceEnabled(normalizedSource)) {
            return DailyHotResult.failure(normalizedSource, "热点来源已关闭").toResponseMap();
        }

        long cacheSeconds = dailyHotProperties.getSafeCacheSeconds();
        String cacheKey = ExternalRedisKeyEnum.DAILY_HOT_KEY.getKey(normalizedSource);
        if (cacheSeconds > 0) {
            Object cache = RedisUtil.get(cacheKey);
            if (cache instanceof Map<?, ?>) {
                Map<Object, Object> result = new LinkedHashMap<>((Map<Object, Object>) cache);
                result.put("fromCache", true);
                return result;
            }
        }

        try {
            DailyHotResult dailyHotResult = provider == null ? builtInDailyHotProvider.fetch(sourceEnum) : provider.fetch();
            Map<Object, Object> result = dailyHotResult.setFromCache(false).toResponseMap();
            if (Boolean.TRUE.equals(dailyHotResult.getSuccess()) && cacheSeconds > 0) {
                RedisUtil.set(cacheKey, result, cacheSeconds);
            }
            return result;
        } catch (Exception e) {
            log.warn("DailyHot fetch failed, source: {}", normalizedSource, e);
            return DailyHotResult.failure(normalizedSource, "获取热点数据失败，请稍后重试").toResponseMap();
        }
    }

    private String normalizeSource(String source) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        return source.trim().toLowerCase(Locale.ROOT);
    }
}
