package com.itwray.iw.external.service.dailyhot.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.itwray.iw.external.core.dailyhot.DailyHotHttpClient;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotItem;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotResult;
import com.itwray.iw.external.model.enums.DailyHotSourceEnum;
import com.itwray.iw.external.service.dailyhot.DailyHotProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微博热搜适配器。
 *
 * @author wray
 * @since 2026/6/26
 */
@Component
public class WeiboDailyHotProvider extends AbstractDailyHotProvider implements DailyHotProvider {

    private static final String HOT_SEARCH_URL = "https://weibo.com/ajax/side/hotSearch";

    private final DailyHotHttpClient dailyHotHttpClient;

    public WeiboDailyHotProvider(DailyHotHttpClient dailyHotHttpClient) {
        this.dailyHotHttpClient = dailyHotHttpClient;
    }

    @Override
    public DailyHotSourceEnum source() {
        return DailyHotSourceEnum.WEIBO;
    }

    @Override
    public DailyHotResult fetch() {
        Map<String, String> headers = new java.util.LinkedHashMap<>();
        headers.put("Referer", "https://weibo.com/");
        JsonNode root = dailyHotHttpClient.getJson(HOT_SEARCH_URL, headers);
        JsonNode listNode = root.path("data").path("realtime");
        List<DailyHotItem> itemList = new ArrayList<>();
        if (listNode.isArray()) {
            for (int i = 0; i < listNode.size(); i++) {
                JsonNode item = listNode.get(i);
                String title = firstText(item, "word", "word_scheme");
                if (StringUtils.isBlank(title)) {
                    title = "热搜" + (i + 1);
                }
                String searchUrl = "https://s.weibo.com/weibo?q=" + encode(title);
                DailyHotItem dailyHotItem = new DailyHotItem()
                        .setId(firstText(item, "mid", "word_scheme", "word"))
                        .setTitle(title)
                        .setDesc(firstText(item, "note", "word_scheme"))
                        .setHot(longValue(item, "num"))
                        .setTimestamp(timestampMillis(item, "onboard_time"))
                        .setUrl(searchUrl)
                        .setMobileUrl(searchUrl);
                itemList.add(dailyHotItem);
            }
        }
        return DailyHotResult.of(source()).setData(itemList).refreshTotal();
    }
}
