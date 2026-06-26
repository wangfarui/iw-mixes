package com.itwray.iw.external.service;

import java.util.Map;

/**
 * 每日热点服务。
 *
 * @author wray
 * @since 2026/6/26
 */
public interface DailyHotService {

    /**
     * 获取每日热点。
     *
     * @param source 来源
     * @return 热点数据
     */
    Map<Object, Object> getDailyHot(String source);
}
