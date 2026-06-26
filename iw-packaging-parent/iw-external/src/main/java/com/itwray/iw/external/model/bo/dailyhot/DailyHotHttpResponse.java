package com.itwray.iw.external.model.bo.dailyhot;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 热榜远程请求响应。
 *
 * @author wray
 * @since 2026/6/26
 */
@Getter
@AllArgsConstructor
public class DailyHotHttpResponse {

    private final int status;

    private final String body;

    private final byte[] bodyBytes;

    private final Map<String, List<String>> headers;
}
