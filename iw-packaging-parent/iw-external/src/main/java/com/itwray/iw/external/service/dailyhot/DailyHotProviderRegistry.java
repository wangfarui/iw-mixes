package com.itwray.iw.external.service.dailyhot;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日热点来源注册表。
 *
 * @author wray
 * @since 2026/6/26
 */
@Component
public class DailyHotProviderRegistry {

    private final Map<String, DailyHotProvider> providerMap = new LinkedHashMap<>();

    public DailyHotProviderRegistry(List<DailyHotProvider> providers) {
        providers.forEach(provider -> providerMap.put(provider.source().getSource(), provider));
    }

    public DailyHotProvider getProvider(String source) {
        return providerMap.get(source);
    }
}
