package com.itwray.iw.external.model.bo.dailyhot;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 热榜条目。
 *
 * @author wray
 * @since 2026/6/26
 */
@Data
@Accessors(chain = true)
public class DailyHotItem {

    private Object id;

    private String title;

    private String cover;

    private String author;

    private String desc;

    private Object hot;

    private Long timestamp;

    private String url;

    private String mobileUrl;

    public Map<String, Object> toResponseMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        putIfNotNull(result, "id", id);
        putIfNotNull(result, "title", title);
        putIfNotNull(result, "cover", cover);
        putIfNotNull(result, "author", author);
        putIfNotNull(result, "desc", desc);
        putIfNotNull(result, "hot", hot);
        putIfNotNull(result, "timestamp", timestamp);
        putIfNotNull(result, "url", url);
        putIfNotNull(result, "mobileUrl", mobileUrl);
        return result;
    }

    private void putIfNotNull(Map<String, Object> result, String key, Object value) {
        if (value != null) {
            result.put(key, value);
        }
    }
}
