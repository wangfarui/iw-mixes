package com.itwray.iw.web.core.webmvc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 请求日志过滤器
 *
 * @author wray
 * @since 2025/3/12
 */
@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return;
        }

        String requestURI = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String paramStr = "";
        if (CollUtil.isNotEmpty(parameterMap)) {
            paramStr = JSONUtil.toJsonStr(parameterMap);
        }
        // 读取请求体
        String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        log.info("web请求入参, 请求接口: {}, 请求param: {}, 请求body: {}", requestURI, paramStr, requestBody);
    }
}
