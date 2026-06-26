package com.itwray.iw.external.service.dailyhot;

import com.itwray.iw.external.model.bo.dailyhot.DailyHotResult;
import com.itwray.iw.external.model.enums.DailyHotSourceEnum;

/**
 * 每日热点来源适配器。
 *
 * @author wray
 * @since 2026/6/26
 */
public interface DailyHotProvider {

    /**
     * 热点来源。
     *
     * @return 来源
     */
    DailyHotSourceEnum source();

    /**
     * 拉取热点。
     *
     * @return 热点数据
     */
    DailyHotResult fetch();
}
