package com.itwray.iw.external.model.bo.dailyhot;

import com.itwray.iw.external.model.enums.DailyHotSourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 每日热点响应。
 *
 * @author wray
 * @since 2026/6/26
 */
@Data
@Accessors(chain = true)
public class DailyHotResult {

    private Integer code = 200;

    private String name;

    private String title;

    private String type;

    private String description;

    private String link;

    private Integer total = 0;

    private String updateTime = Instant.now().toString();

    private Boolean fromCache = false;

    private Boolean success = true;

    private String message;

    private List<DailyHotItem> data = new ArrayList<>();

    public static DailyHotResult of(DailyHotSourceEnum source) {
        return new DailyHotResult()
                .setName(source.getSource())
                .setTitle(source.getTitle())
                .setType(source.getType())
                .setDescription(source.getDescription())
                .setLink(source.getLink());
    }

    public static DailyHotResult failure(String source, String message) {
        DailyHotSourceEnum sourceEnum = DailyHotSourceEnum.of(source);
        DailyHotResult result = sourceEnum == null ? new DailyHotResult().setName(source) : of(sourceEnum);
        return result.setCode(500)
                .setSuccess(false)
                .setMessage(message)
                .setData(new ArrayList<>())
                .refreshTotal();
    }

    public DailyHotResult refreshTotal() {
        this.total = data == null ? 0 : data.size();
        return this;
    }

    public Map<Object, Object> toResponseMap() {
        refreshTotal();
        Map<Object, Object> result = new LinkedHashMap<>();
        putIfNotNull(result, "code", code);
        putIfNotBlank(result, "name", name);
        putIfNotBlank(result, "title", title);
        putIfNotBlank(result, "type", type);
        putIfNotBlank(result, "description", description);
        putIfNotBlank(result, "link", link);
        putIfNotNull(result, "total", total);
        putIfNotBlank(result, "updateTime", updateTime);
        putIfNotNull(result, "fromCache", fromCache);
        putIfNotNull(result, "success", success);
        putIfNotBlank(result, "message", message);
        List<Map<String, Object>> itemList = new ArrayList<>();
        if (data != null) {
            data.forEach(item -> itemList.add(item.toResponseMap()));
        }
        result.put("data", itemList);
        return result;
    }

    private void putIfNotNull(Map<Object, Object> result, String key, Object value) {
        if (value != null) {
            result.put(key, value);
        }
    }

    private void putIfNotBlank(Map<Object, Object> result, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            result.put(key, value);
        }
    }
}
