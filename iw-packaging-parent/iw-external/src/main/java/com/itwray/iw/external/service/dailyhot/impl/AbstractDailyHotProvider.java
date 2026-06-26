package com.itwray.iw.external.service.dailyhot.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.itwray.iw.external.model.bo.dailyhot.DailyHotItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 每日热点来源适配器基础能力。
 *
 * @author wray
 * @since 2026/6/26
 */
public abstract class AbstractDailyHotProvider {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");

    protected String text(JsonNode node, String fieldName) {
        if (node == null || node.get(fieldName) == null || node.get(fieldName).isNull()) {
            return null;
        }
        return node.get(fieldName).asText();
    }

    protected JsonNode nodeAt(JsonNode node, String path) {
        if (node == null || StringUtils.isBlank(path)) {
            return MissingNode.getInstance();
        }
        JsonNode current = node;
        for (String part : path.split("\\.")) {
            if (current == null || current.isMissingNode() || current.isNull()) {
                return MissingNode.getInstance();
            }
            if (StringUtils.isNumeric(part) && current.isArray()) {
                int index = Integer.parseInt(part);
                current = index < current.size() ? current.get(index) : MissingNode.getInstance();
            } else {
                current = current.path(part);
            }
        }
        return current == null ? MissingNode.getInstance() : current;
    }

    protected String textAt(JsonNode node, String path) {
        JsonNode valueNode = nodeAt(node, path);
        if (valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }
        return valueNode.asText();
    }

    protected String firstTextAt(JsonNode node, String... paths) {
        for (String path : paths) {
            String value = textAt(node, path);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    protected String firstText(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            String value = text(node, fieldName);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    protected Long longValue(JsonNode node, String fieldName) {
        if (node == null || node.get(fieldName) == null || node.get(fieldName).isNull()) {
            return null;
        }
        JsonNode valueNode = node.get(fieldName);
        if (valueNode.isNumber()) {
            return valueNode.asLong();
        }
        String value = valueNode.asText();
        if (!StringUtils.isNumeric(value)) {
            return null;
        }
        return Long.parseLong(value);
    }

    protected Long longAt(JsonNode node, String path) {
        JsonNode valueNode = nodeAt(node, path);
        if (valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }
        if (valueNode.isNumber()) {
            return valueNode.asLong();
        }
        String value = valueNode.asText();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Matcher matcher = NUMBER_PATTERN.matcher(value.replace(",", ""));
        return matcher.find() ? new BigDecimal(matcher.group()).longValue() : null;
    }

    protected Long timestampMillis(JsonNode node, String fieldName) {
        Long timestamp = longValue(node, fieldName);
        if (timestamp == null) {
            return null;
        }
        return timestamp < 1_000_000_000_000L ? timestamp * 1000 : timestamp;
    }

    protected Long timestampMillisAt(JsonNode node, String path) {
        Long timestamp = longAt(node, path);
        if (timestamp == null) {
            return null;
        }
        return timestamp < 1_000_000_000_000L ? timestamp * 1000 : timestamp;
    }

    protected String httpsUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        return url.replaceFirst("^http:", "https:");
    }

    protected String encode(String value) {
        if (value == null) {
            return "";
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    protected String stripHtml(String html) {
        if (StringUtils.isBlank(html)) {
            return html;
        }
        return Jsoup.parse(html).text().trim();
    }

    protected String absoluteUrl(String url, String baseUrl) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        if (url.startsWith("//")) {
            return "https:" + url;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        if (StringUtils.isBlank(baseUrl)) {
            return url;
        }
        if (url.startsWith("/")) {
            String origin = baseUrl.replaceFirst("^(https?://[^/]+).*$", "$1");
            return origin + url;
        }
        return baseUrl.replaceFirst("/+$", "") + "/" + url;
    }

    protected Long parseHotText(String hotText) {
        if (StringUtils.isBlank(hotText)) {
            return null;
        }
        Matcher matcher = NUMBER_PATTERN.matcher(hotText);
        if (!matcher.find()) {
            return null;
        }
        BigDecimal value = new BigDecimal(matcher.group());
        if (hotText.contains("亿")) {
            value = value.multiply(BigDecimal.valueOf(100_000_000L));
        } else if (hotText.contains("万")) {
            value = value.multiply(BigDecimal.valueOf(10_000L));
        }
        return value.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    protected Long parseTime(String timeText) {
        if (StringUtils.isBlank(timeText)) {
            return null;
        }
        try {
            return ZonedDateTime.parse(timeText, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant().toEpochMilli();
        } catch (Exception ignored) {
            try {
                return Instant.parse(timeText).toEpochMilli();
            } catch (Exception ignoredAgain) {
                return null;
            }
        }
    }

    protected List<DailyHotItem> parseRss(String xml, String baseUrl) {
        Document document = Jsoup.parse(xml, "", Parser.xmlParser());
        Elements items = document.select("item");
        List<DailyHotItem> result = new ArrayList<>();
        items.forEach(item -> {
            String title = item.selectFirst("title") == null ? null : item.selectFirst("title").text();
            String link = item.selectFirst("link") == null ? null : item.selectFirst("link").text();
            String desc = item.selectFirst("description") == null ? null : stripHtml(item.selectFirst("description").text());
            String pubDate = item.selectFirst("pubDate") == null ? null : item.selectFirst("pubDate").text();
            String id = item.selectFirst("guid") == null ? link : item.selectFirst("guid").text();
            result.add(new DailyHotItem()
                    .setId(id)
                    .setTitle(title)
                    .setDesc(desc)
                    .setTimestamp(parseTime(pubDate))
                    .setUrl(absoluteUrl(link, baseUrl))
                    .setMobileUrl(absoluteUrl(link, baseUrl)));
        });
        return result;
    }
}
