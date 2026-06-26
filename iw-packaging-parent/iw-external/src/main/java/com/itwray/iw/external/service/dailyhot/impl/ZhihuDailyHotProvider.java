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

/**
 * 知乎热榜适配器。
 *
 * @author wray
 * @since 2026/6/26
 */
@Component
public class ZhihuDailyHotProvider extends AbstractDailyHotProvider implements DailyHotProvider {

    private static final String HOT_LIST_URL = "https://api.zhihu.com/topstory/hot-lists/total?limit=50";

    private final DailyHotHttpClient dailyHotHttpClient;

    public ZhihuDailyHotProvider(DailyHotHttpClient dailyHotHttpClient) {
        this.dailyHotHttpClient = dailyHotHttpClient;
    }

    @Override
    public DailyHotSourceEnum source() {
        return DailyHotSourceEnum.ZHIHU;
    }

    @Override
    public DailyHotResult fetch() {
        JsonNode root = dailyHotHttpClient.getJson(HOT_LIST_URL);
        JsonNode listNode = root.path("data");
        List<DailyHotItem> itemList = new ArrayList<>();
        if (listNode.isArray()) {
            listNode.forEach(item -> {
                JsonNode target = item.path("target");
                String targetUrl = text(target, "url");
                String questionId = getQuestionId(targetUrl);
                String url = StringUtils.isBlank(questionId) ? "https://www.zhihu.com/hot" : "https://www.zhihu.com/question/" + questionId;
                DailyHotItem dailyHotItem = new DailyHotItem()
                        .setId(text(target, "id"))
                        .setTitle(text(target, "title"))
                        .setDesc(text(target, "excerpt"))
                        .setCover(getCover(item))
                        .setHot(parseHotText(text(item, "detail_text")))
                        .setTimestamp(timestampMillis(target, "created"))
                        .setUrl(url)
                        .setMobileUrl(url);
                itemList.add(dailyHotItem);
            });
        }
        return DailyHotResult.of(source()).setData(itemList).refreshTotal();
    }

    private String getCover(JsonNode item) {
        JsonNode children = item.path("children");
        if (!children.isArray() || children.size() == 0) {
            return null;
        }
        return text(children.get(0), "thumbnail");
    }

    private String getQuestionId(String targetUrl) {
        if (StringUtils.isBlank(targetUrl)) {
            return null;
        }
        String[] parts = targetUrl.split("/");
        return parts.length == 0 ? null : parts[parts.length - 1];
    }
}
