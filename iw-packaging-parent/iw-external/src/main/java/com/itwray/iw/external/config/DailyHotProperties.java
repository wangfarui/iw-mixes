package com.itwray.iw.external.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 每日热点配置。
 *
 * @author wray
 * @since 2026/6/26
 */
@Data
@ConfigurationProperties(prefix = "iw.external.dailyhot")
public class DailyHotProperties {

    /**
     * 远程站点请求超时时间，单位：毫秒。
     */
    private Integer timeoutMs = 3000;

    /**
     * 热点短缓存时间，单位：秒。小于等于 0 时不缓存。
     */
    private Long cacheSeconds = 1800L;

    /**
     * 来源开关配置。未配置或 enabled 为空时默认启用。
     */
    private Map<String, SourceProperties> sources = new LinkedHashMap<>();

    public int getSafeTimeoutMs() {
        if (timeoutMs == null || timeoutMs <= 0) {
            return 3000;
        }
        return timeoutMs;
    }

    public long getSafeCacheSeconds() {
        if (cacheSeconds == null) {
            return 1800L;
        }
        return cacheSeconds;
    }

    public boolean isSourceEnabled(String source) {
        SourceProperties sourceProperties = getSourceProperties(source);
        return sourceProperties == null || sourceProperties.getEnabled() == null || sourceProperties.getEnabled();
    }

    public SourceProperties getSourceProperties(String source) {
        if (StringUtils.isBlank(source) || sources == null || sources.isEmpty()) {
            return null;
        }
        SourceProperties sourceProperties = sources.get(source);
        if (sourceProperties != null) {
            return sourceProperties;
        }
        sourceProperties = sources.get(source.toLowerCase(Locale.ROOT));
        if (sourceProperties != null) {
            return sourceProperties;
        }
        for (Map.Entry<String, SourceProperties> entry : sources.entrySet()) {
            if (source.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Data
    public static class SourceProperties {

        /**
         * 来源是否启用。为空时默认启用。
         */
        private Boolean enabled;
    }
}
