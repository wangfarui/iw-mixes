package com.itwray.iw.external.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 静态博客受保护文章访问配置。
 *
 * @author wray
 * @since 2026/6/24
 */
@Data
@ConfigurationProperties(prefix = "blog.access")
public class BlogAccessProperties {

    /**
     * 允许跨域访问的博客域名，多个值用英文逗号分隔。
     */
    private String allowedOrigins;

    /**
     * 博客访问令牌签名密钥。
     */
    private String tokenSecret;

    /**
     * 默认访问令牌过期时间，单位：秒。
     */
    private Long defaultExpiresIn = 86400L;

    /**
     * expiresAt 字段输出时使用的时区。
     */
    private String expiresAtZoneId = "Asia/Shanghai";

    /**
     * scope 访问配置。
     */
    private Map<String, ScopeProperties> scope = new LinkedHashMap<>();

    /**
     * 失败请求限流配置。
     */
    private RateLimitProperties rateLimit = new RateLimitProperties();

    public Set<String> allowedOriginSet() {
        if (StringUtils.isBlank(allowedOrigins)) {
            return Collections.emptySet();
        }
        Set<String> result = new LinkedHashSet<>();
        Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .forEach(result::add);
        return result;
    }

    public boolean isAllowedOrigin(String origin) {
        return StringUtils.isBlank(origin) || allowedOriginSet().contains(origin);
    }

    public ScopeProperties getScopeProperties(String scopeName) {
        if (StringUtils.isBlank(scopeName) || scope == null || scope.isEmpty()) {
            return null;
        }
        ScopeProperties scopeProperties = scope.get(scopeName);
        if (scopeProperties != null) {
            return scopeProperties;
        }
        scopeProperties = scope.get(scopeName.toLowerCase());
        if (scopeProperties != null) {
            return scopeProperties;
        }
        scopeProperties = scope.get(scopeName.toUpperCase());
        if (scopeProperties != null) {
            return scopeProperties;
        }
        for (Map.Entry<String, ScopeProperties> entry : scope.entrySet()) {
            if (scopeName.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public long getSafeDefaultExpiresIn() {
        if (defaultExpiresIn == null || defaultExpiresIn <= 0) {
            return 86400L;
        }
        return defaultExpiresIn;
    }

    public ZoneId getExpiresAtZone() {
        if (StringUtils.isBlank(expiresAtZoneId)) {
            return ZoneId.of("Asia/Shanghai");
        }
        return ZoneId.of(expiresAtZoneId);
    }

    @Data
    public static class ScopeProperties {

        /**
         * BCrypt/Argon2 等不可逆密码 hash。目前项目统一使用 BCrypt。
         */
        private String passwordHash;

        /**
         * base64url 编码的 32 字节 AES key。
         */
        private String key;
    }

    @Data
    public static class RateLimitProperties {

        /**
         * 同一 IP + scope 在窗口内允许的失败次数。
         */
        private Integer maxFailures = 5;

        /**
         * 限流窗口，单位：秒。
         */
        private Long windowSeconds = 60L;

        public int getSafeMaxFailures() {
            if (maxFailures == null || maxFailures <= 0) {
                return 5;
            }
            return maxFailures;
        }

        public long getSafeWindowSeconds() {
            if (windowSeconds == null || windowSeconds <= 0) {
                return 60L;
            }
            return windowSeconds;
        }
    }
}
