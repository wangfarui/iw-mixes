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

/**
 * 哔哩哔哩热榜适配器。
 *
 * @author wray
 * @since 2026/6/26
 */
@Component
public class BilibiliDailyHotProvider extends AbstractDailyHotProvider implements DailyHotProvider {

    private static final String RANKING_URL = "https://api.bilibili.com/x/web-interface/ranking?jsonp=jsonp&rid=0&type=all";

    private final DailyHotHttpClient dailyHotHttpClient;

    public BilibiliDailyHotProvider(DailyHotHttpClient dailyHotHttpClient) {
        this.dailyHotHttpClient = dailyHotHttpClient;
    }

    @Override
    public DailyHotSourceEnum source() {
        return DailyHotSourceEnum.BILIBILI;
    }

    @Override
    public DailyHotResult fetch() {
        Map<String, String> headers = new java.util.LinkedHashMap<>();
        headers.put("Referer", "https://www.bilibili.com/ranking/all");
        JsonNode root = dailyHotHttpClient.getJson(RANKING_URL, headers);
        JsonNode listNode = root.path("data").path("list");
        List<DailyHotItem> itemList = new ArrayList<>();
        if (listNode.isArray()) {
            listNode.forEach(item -> {
                String bvid = text(item, "bvid");
                String videoUrl = "https://www.bilibili.com/video/" + bvid;
                DailyHotItem dailyHotItem = new DailyHotItem()
                        .setId(bvid)
                        .setTitle(text(item, "title"))
                        .setDesc(firstText(item, "desc", "description"))
                        .setCover(httpsUrl(text(item, "pic")))
                        .setAuthor(firstText(item, "author", "owner_name"))
                        .setHot(firstHot(item))
                        .setTimestamp(timestampMillis(item, "pubdate"))
                        .setUrl(videoUrl)
                        .setMobileUrl("https://m.bilibili.com/video/" + bvid);
                itemList.add(dailyHotItem);
            });
        }
        return DailyHotResult.of(source()).setData(itemList).refreshTotal();
    }

    private Object firstHot(JsonNode item) {
        Long videoReview = longValue(item, "video_review");
        if (videoReview != null) {
            return videoReview;
        }
        Long play = longValue(item, "play");
        if (play != null) {
            return play;
        }
        JsonNode stat = item.path("stat");
        Long view = longValue(stat, "view");
        return view == null ? 0L : view;
    }
}
