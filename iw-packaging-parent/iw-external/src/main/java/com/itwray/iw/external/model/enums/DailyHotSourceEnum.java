package com.itwray.iw.external.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * 每日热点来源。
 *
 * @author wray
 * @since 2026/6/26
 */
@Getter
public enum DailyHotSourceEnum {

    BILIBILI("bilibili", "哔哩哔哩", "热榜 · 全站", "你所热爱的，就是你的生活", "https://www.bilibili.com/v/popular/rank/all"),
    WEIBO("weibo", "微博", "热搜榜", "实时热点，每分钟更新一次", "https://s.weibo.com/top/summary/"),
    ZHIHU("zhihu", "知乎", "热榜", null, "https://www.zhihu.com/hot"),
    DOUYIN("douyin", "抖音", "热榜", "实时上升热点", "https://www.douyin.com");

    private final String source;

    private final String title;

    private final String type;

    private final String description;

    private final String link;

    DailyHotSourceEnum(String source, String title, String type, String description, String link) {
        this.source = source;
        this.title = title;
        this.type = type;
        this.description = description;
        this.link = link;
    }

    public static DailyHotSourceEnum of(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        String normalized = source.trim().toLowerCase(Locale.ROOT);
        for (DailyHotSourceEnum sourceEnum : values()) {
            if (sourceEnum.source.equals(normalized)) {
                return sourceEnum;
            }
        }
        return null;
    }
}
