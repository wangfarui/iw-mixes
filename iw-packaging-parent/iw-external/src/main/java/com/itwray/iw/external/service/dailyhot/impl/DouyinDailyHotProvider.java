package com.itwray.iw.external.service.dailyhot.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.itwray.iw.external.core.dailyhot.DailyHotHttpClient;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotItem;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotResult;
import com.itwray.iw.external.model.enums.DailyHotSourceEnum;
import com.itwray.iw.external.service.dailyhot.DailyHotProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 抖音热榜适配器。
 *
 * @author wray
 * @since 2026/6/26
 */
@Component
public class DouyinDailyHotProvider extends AbstractDailyHotProvider implements DailyHotProvider {

    private static final String HOT_LIST_URL = "https://www.douyin.com/aweme/v1/web/hot/search/list/"
            + "?device_platform=webapp&aid=6383&channel=channel_pc_web&detail_list=1";

    private final DailyHotHttpClient dailyHotHttpClient;

    public DouyinDailyHotProvider(DailyHotHttpClient dailyHotHttpClient) {
        this.dailyHotHttpClient = dailyHotHttpClient;
    }

    @Override
    public DailyHotSourceEnum source() {
        return DailyHotSourceEnum.DOUYIN;
    }

    @Override
    public DailyHotResult fetch() {
        Map<String, String> headers = new java.util.LinkedHashMap<>();
        headers.put("Referer", "https://www.douyin.com/");
        headers.put("Cookie", "passport_csrf_token=" + UUID.randomUUID());
        JsonNode root = dailyHotHttpClient.getJson(HOT_LIST_URL, headers);
        JsonNode listNode = root.path("data").path("word_list");
        List<DailyHotItem> itemList = new ArrayList<>();
        if (listNode.isArray()) {
            listNode.forEach(item -> {
                String sentenceId = text(item, "sentence_id");
                String url = "https://www.douyin.com/hot/" + sentenceId;
                DailyHotItem dailyHotItem = new DailyHotItem()
                        .setId(sentenceId)
                        .setTitle(text(item, "word"))
                        .setHot(longValue(item, "hot_value"))
                        .setTimestamp(timestampMillis(item, "event_time"))
                        .setUrl(url)
                        .setMobileUrl(url);
                itemList.add(dailyHotItem);
            });
        }
        return DailyHotResult.of(source()).setData(itemList).refreshTotal();
    }
}
