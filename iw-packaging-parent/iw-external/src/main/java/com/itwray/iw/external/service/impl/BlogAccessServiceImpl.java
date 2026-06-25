package com.itwray.iw.external.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.external.config.BlogAccessProperties;
import com.itwray.iw.external.model.dto.BlogAccessVerifyDto;
import com.itwray.iw.external.model.vo.BlogAccessVerifyVo;
import com.itwray.iw.external.service.BlogAccessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 博客文章访问服务实现。
 *
 * @author wray
 * @since 2026/6/24
 */
@Slf4j
@Service
public class BlogAccessServiceImpl implements BlogAccessService {

    private static final String FAIL_MESSAGE = "访问密码不正确";

    private static final Pattern BASE64_URL_32_BYTES_KEY_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{43}$");

    private final BlogAccessProperties properties;

    private final BlogAccessRateLimiter rateLimiter;

    private final Clock clock;

    @Autowired
    public BlogAccessServiceImpl(BlogAccessProperties properties, BlogAccessRateLimiter rateLimiter) {
        this(properties, rateLimiter, Clock.systemUTC());
    }

    BlogAccessServiceImpl(BlogAccessProperties properties, BlogAccessRateLimiter rateLimiter, Clock clock) {
        this.properties = properties;
        this.rateLimiter = rateLimiter;
        this.clock = clock;
    }

    @Override
    public BlogAccessVerifyVo verify(BlogAccessVerifyDto dto, String clientIp, String userAgent) {
        Instant now = clock.instant();
        String scope = normalizeScope(dto == null ? null : dto.getScope());
        boolean success = false;
        try {
            if (!isCompleteRequest(dto)) {
                rateLimiter.recordFailure(clientIp, scope);
                return failure();
            }
            if (rateLimiter.isBlocked(clientIp, scope)) {
                return failure();
            }

            BlogAccessProperties.ScopeProperties scopeProperties = properties.getScopeProperties(scope);
            if (scopeProperties == null || StringUtils.isBlank(scopeProperties.getPasswordHash())) {
                rateLimiter.recordFailure(clientIp, scope);
                return failure();
            }
            if (StringUtils.isBlank(scopeProperties.getKey()) || !isValidAesKey(scopeProperties.getKey())) {
                log.error("博客访问scope配置异常，AES key必须是base64url编码的32字节随机值，scope={}", scope);
                return failure();
            }
            if (StringUtils.isBlank(properties.getTokenSecret())) {
                log.error("博客访问配置异常，BLOG_ACCESS_TOKEN_SECRET不能为空");
                return failure();
            }
            if (!checkPassword(dto.getPassword(), scopeProperties.getPasswordHash(), scope)) {
                rateLimiter.recordFailure(clientIp, scope);
                return failure();
            }

            Instant expiresAt = now.plusSeconds(properties.getSafeDefaultExpiresIn());
            String token = createToken(dto, scope, now, expiresAt);
            String expiresAtText = formatExpiresAt(expiresAt);
            rateLimiter.clear(clientIp, scope);
            success = true;
            return BlogAccessVerifyVo.success(scope, dto.getPostId(), token, expiresAtText, scopeProperties.getKey());
        } finally {
            audit(dto, scope, clientIp, userAgent, success, now);
        }
    }

    private boolean isCompleteRequest(BlogAccessVerifyDto dto) {
        return dto != null
                && StringUtils.isNoneBlank(dto.getPostId(), dto.getPath(), dto.getScope(), dto.getPassword());
    }

    private String normalizeScope(String scope) {
        return StringUtils.trimToEmpty(scope).toLowerCase(Locale.ROOT);
    }

    private boolean checkPassword(String rawPassword, String passwordHash, String scope) {
        try {
            return BCrypt.checkpw(rawPassword, passwordHash);
        } catch (RuntimeException e) {
            log.error("博客访问scope配置异常，密码hash无法使用BCrypt校验，scope={}", scope, e);
            return false;
        }
    }

    private boolean isValidAesKey(String key) {
        if (!BASE64_URL_32_BYTES_KEY_PATTERN.matcher(key).matches()) {
            return false;
        }
        try {
            return Base64.getUrlDecoder().decode(key).length == 32;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String createToken(BlogAccessVerifyDto dto, String scope, Instant issuedAt, Instant expiresAt) {
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("iss", "iw-blog-access");
        payload.put("aud", "hexo-blog");
        payload.put("sub", scope);
        payload.put("access", "scope");
        payload.put("scope", scope);
        payload.put("postId", dto.getPostId());
        payload.put("path", dto.getPath());
        payload.put("iat", issuedAt.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());
        payload.put("jti", UUID.randomUUID().toString());

        String encodedHeader = base64Url(JSONUtil.toJsonStr(header).getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64Url(JSONUtil.toJsonStr(payload).getBytes(StandardCharsets.UTF_8));
        String signingInput = encodedHeader + "." + encodedPayload;
        return signingInput + "." + sign(signingInput);
    }

    private String sign(String signingInput) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(properties.getTokenSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Url(mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("博客访问token签名失败", e);
        }
    }

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String formatExpiresAt(Instant expiresAt) {
        OffsetDateTime offsetDateTime = expiresAt.atZone(properties.getExpiresAtZone()).toOffsetDateTime();
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime);
    }

    private BlogAccessVerifyVo failure() {
        return BlogAccessVerifyVo.fail(FAIL_MESSAGE);
    }

    private void audit(BlogAccessVerifyDto dto, String scope, String clientIp, String userAgent, boolean success, Instant time) {
        String postId = dto == null ? null : dto.getPostId();
        String path = dto == null ? null : dto.getPath();
        log.info("blog_access_audit scope={} postId={} path={} ip={} userAgent={} success={} time={}",
                safeLogValue(scope),
                safeLogValue(postId),
                safeLogValue(path),
                safeLogValue(clientIp),
                safeLogValue(userAgent),
                success,
                DateTimeFormatter.ISO_INSTANT.format(time));
    }

    private String safeLogValue(String value) {
        if (value == null) {
            return "";
        }
        String sanitized = value.replace('\r', ' ')
                .replace('\n', ' ')
                .replace('\t', ' ');
        return StringUtils.abbreviate(sanitized, 500);
    }
}
