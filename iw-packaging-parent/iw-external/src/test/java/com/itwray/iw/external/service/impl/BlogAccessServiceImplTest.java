package com.itwray.iw.external.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.itwray.iw.external.config.BlogAccessProperties;
import com.itwray.iw.external.model.dto.BlogAccessVerifyDto;
import com.itwray.iw.external.model.vo.BlogAccessVerifyVo;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class BlogAccessServiceImplTest {

    @Test
    void verifySuccessReturnsScopeKeyAndToken() {
        BlogAccessServiceImpl service = createService("open-sesame", testAesKey(), 3600L, 5);

        BlogAccessVerifyVo vo = service.verify(createDto("open-sesame"), "127.0.0.1", "JUnit");

        assertTrue(vo.isOk());
        assertEquals("scope", vo.getAccess());
        assertEquals("life", vo.getScope());
        assertEquals("2026/02/24/monthly-202602", vo.getPostId());
        assertEquals(testAesKey(), vo.getKey());
        assertEquals("2026-06-25T00:00:00+08:00", vo.getExpiresAt());
        assertNotNull(vo.getToken());
        assertEquals(3, vo.getToken().split("\\.").length);
        assertNull(vo.getMessage());
    }

    @Test
    void verifyFailureIsGenericAndLimitedByIpAndScope() {
        BlogAccessServiceImpl service = createService("open-sesame", testAesKey(), 3600L, 2);

        BlogAccessVerifyVo first = service.verify(createDto("wrong-1"), "127.0.0.1", "JUnit");
        BlogAccessVerifyVo second = service.verify(createDto("wrong-2"), "127.0.0.1", "JUnit");
        BlogAccessVerifyVo blocked = service.verify(createDto("open-sesame"), "127.0.0.1", "JUnit");

        assertFalse(first.isOk());
        assertFalse(second.isOk());
        assertFalse(blocked.isOk());
        assertEquals("访问密码不正确", blocked.getMessage());
        assertNull(blocked.getKey());
        assertNull(blocked.getToken());
    }

    @Test
    void verifyRejectsInvalidAesKey() {
        BlogAccessServiceImpl service = createService("open-sesame", "not-a-32-byte-key", 3600L, 5);

        BlogAccessVerifyVo vo = service.verify(createDto("open-sesame"), "127.0.0.1", "JUnit");

        assertFalse(vo.isOk());
        assertEquals("访问密码不正确", vo.getMessage());
        assertNull(vo.getKey());
    }

    private BlogAccessServiceImpl createService(String password, String key, Long expiresIn, Integer maxFailures) {
        BlogAccessProperties properties = new BlogAccessProperties();
        properties.setTokenSecret("test-token-secret-with-enough-randomness");
        properties.setDefaultExpiresIn(expiresIn);
        properties.getRateLimit().setMaxFailures(maxFailures);
        properties.getRateLimit().setWindowSeconds(60L);

        BlogAccessProperties.ScopeProperties scopeProperties = new BlogAccessProperties.ScopeProperties();
        scopeProperties.setPasswordHash(BCrypt.hashpw(password));
        scopeProperties.setKey(key);
        properties.getScope().put("life", scopeProperties);

        BlogAccessRateLimiter rateLimiter = new BlogAccessRateLimiter(properties);
        Clock clock = Clock.fixed(Instant.parse("2026-06-24T15:00:00Z"), ZoneOffset.UTC);
        return new BlogAccessServiceImpl(properties, rateLimiter, clock);
    }

    private BlogAccessVerifyDto createDto(String password) {
        BlogAccessVerifyDto dto = new BlogAccessVerifyDto();
        dto.setPostId("2026/02/24/monthly-202602");
        dto.setPath("/2026/02/24/monthly-202602/");
        dto.setScope("life");
        dto.setPassword(password);
        return dto;
    }

    private String testAesKey() {
        byte[] keyBytes = "12345678901234567890123456789012".getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }
}
