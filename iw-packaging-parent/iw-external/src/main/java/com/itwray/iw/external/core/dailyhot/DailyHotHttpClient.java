package com.itwray.iw.external.core.dailyhot;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwray.iw.external.config.DailyHotProperties;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日热点远程请求客户端。
 *
 * @author wray
 * @since 2026/6/26
 */
@Slf4j
@Component
public class DailyHotHttpClient {

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36";

    private final DailyHotProperties dailyHotProperties;

    private final ObjectMapper objectMapper;

    public DailyHotHttpClient(DailyHotProperties dailyHotProperties, ObjectMapper objectMapper) {
        this.dailyHotProperties = dailyHotProperties;
        this.objectMapper = objectMapper;
    }

    public JsonNode getJson(String url) {
        return getJson(url, Collections.emptyMap());
    }

    public JsonNode getJson(String url, Map<String, String> headers) {
        try {
            DailyHotHttpResponse response = get(url, headers);
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new IllegalStateException("每日热点远程响应解析异常", e);
        }
    }

    public JsonNode postJson(String url, Object body, Map<String, String> headers) {
        try {
            DailyHotHttpResponse response = post(url, body, headers);
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new IllegalStateException("每日热点远程响应解析异常", e);
        }
    }

    public DailyHotHttpResponse get(String url, Map<String, String> headers) {
        return execute(HttpUtil.createGet(url), url, headers);
    }

    public DailyHotHttpResponse post(String url, Object body, Map<String, String> headers) {
        String bodyText;
        try {
            bodyText = body instanceof String ? (String) body : objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new IllegalStateException("每日热点请求体序列化异常", e);
        }
        String contentType = ContentType.JSON.getValue();
        if (headers != null && headers.get("Content-Type") != null) {
            contentType = headers.get("Content-Type");
        }
        HttpRequest request = HttpUtil.createPost(url).body(bodyText, contentType);
        return execute(request, url, headers);
    }

    private DailyHotHttpResponse execute(HttpRequest request, String url, Map<String, String> headers) {
        long start = System.currentTimeMillis();
        request.timeout(dailyHotProperties.getSafeTimeoutMs());
        Map<String, String> requestHeaders = new LinkedHashMap<>();
        requestHeaders.put("User-Agent", DEFAULT_USER_AGENT);
        if (headers != null) {
            requestHeaders.putAll(headers);
        }
        requestHeaders.forEach(request::header);
        try (HttpResponse response = request.execute()) {
            int status = response.getStatus();
            long cost = System.currentTimeMillis() - start;
            log.info("DailyHot remote get completed, status: {}, cost: {}ms, url: {}", status, cost, url);
            if (status < 200 || status >= 300) {
                throw new IllegalStateException("每日热点远程请求失败, status: " + status);
            }
            Map<String, List<String>> responseHeaders = response.headers();
            return new DailyHotHttpResponse(status, response.body(), response.bodyBytes(), responseHeaders);
        }
    }
}
