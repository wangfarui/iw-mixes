package com.itwray.iw.external.service.dailyhot.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    protected Long timestampMillis(JsonNode node, String fieldName) {
        Long timestamp = longValue(node, fieldName);
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
}
