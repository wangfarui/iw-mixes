package com.itwray.iw.web.core.dingtalk;

import com.itwray.iw.common.utils.ExceptionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.annotation.Transient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

/**
 * 钉钉属性配置信息
 *
 * @author wangfarui
 * @since 2025/1/6
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = DingTalkConfiguration.DING_TALK_PREFIX)
public class DingTalkProperties implements InitializingBean {

    /**
     * 钉钉机器人客户端地址
     */
    private String clientUrl = "https://oapi.dingtalk.com/robot/send";

    /**
     * 钉钉机器人访问token
     */
    private String accessToken;

    /**
     * 钉钉机器人密钥
     */
    private String secret;

    /**
     * 开启钉钉告警功能
     */
    private Boolean enabled;

    /**
     * @ 对象
     */
    private AT at = new AT();

    /**
     * 忽略的告警url
     */
    private Set<String> ignoreUrls;

    /**
     * 指定的告警url
     */
    private Set<String> warnUrls;

    /**
     * 开启全部告警
     * <p>所有异常均会钉钉告警通知</p>
     */
    private boolean openAllWarning;

    /**
     * 有secret加签时的最新时间戳（保持在半小时以内）
     */
    @Transient
    private long newestTimestamp;

    /**
     * 有密钥时，阻塞获取 newestTimestamp 值
     */
    @Transient
    private final Object LOCK = new Object();

    /**
     * 全量请求url地址
     */
    private static String REQUEST_URL_CACHE;

    /**
     * 请求url地址的缓存时长（毫秒）
     */
    private static final long REQUEST_URL_CACHE_DURATION = 30 * 60 * 1000;

    private static final String[] REQUEST_URL_TEMPLATE = {"%s?access_token=%s", "%s?access_token=%s&timestamp=%s&sign=%s"};

    private static final String[] MONITOR_PROPERTIES_PARAMS = {"dingTalk.client-url", "dingTalk.access-token", "dingTalk.secret"};

    /**
     * 获取钉钉客户端请求路径url
     *
     * @return 全量请求路径url
     */
    public String getRequestUrl() {
        // 校验钉钉配置
        valid();

        if (StringUtils.isNotBlank(this.secret)) {
            synchronized (LOCK) {
                long nowTimestamp = System.currentTimeMillis();
                // 缓存为空 或 缓存超时
                if (REQUEST_URL_CACHE == null || (nowTimestamp - this.newestTimestamp > REQUEST_URL_CACHE_DURATION)) {
                    String sign = getCryptoSign(nowTimestamp);
                    REQUEST_URL_CACHE = generateRequestUrl(nowTimestamp, sign);
                    this.newestTimestamp = nowTimestamp;
                }
            }
        } else if (REQUEST_URL_CACHE == null) {
            REQUEST_URL_CACHE = generateRequestUrl(0L, null);
        }

        return REQUEST_URL_CACHE;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            valid();
            DingTalkRobotClient.changeCanApply(this.enabled);
            log.info("钉钉告警初始化配置: {}", this);
        } catch (IllegalArgumentException e) {
            this.enabled = false;
        }
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        REQUEST_URL_CACHE = null;
    }

    @Override
    public String toString() {
        return "DingTalkProperties{" +
                "enabled=" + enabled +
                ", atMobiles=" + at.getAtMobiles() +
                ", atAll=" + at.getAtAll() +
                ", ignoreUrls=" + ignoreUrls +
                ", warnUrls=" + warnUrls +
                ", openAllWarning=" + openAllWarning +
                '}';
    }

    private void valid() {
        if (StringUtils.isBlank(this.clientUrl)) {
            throw new IllegalArgumentException("钉钉url地址不能为空!");
        }
    }

    private String getCryptoSign(long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + this.secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(this.secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            log.warn("[DingTalkClient][getCryptoSign]钉钉客户端获取加密签名异常。" + ExceptionUtils.exceptionStackTraceText(e));
        }
        log.warn("[DingTalkClient][getCryptoSign]钉钉客户端获取加密签名未知异常，请检查秘钥配置信息！");
        return null;
    }

    private String generateRequestUrl(long timestamp, String sign) {
        if (timestamp != 0L && StringUtils.isNotBlank(sign)) {
            return String.format(REQUEST_URL_TEMPLATE[1], this.clientUrl, this.accessToken, timestamp, sign);
        } else {
            return String.format(REQUEST_URL_TEMPLATE[0], this.clientUrl, this.accessToken);
        }
    }

    @Getter
    @Setter
    public static class AT {
        /**
         * 群@指定人手机号
         */
        private List<String> atMobiles;

        /**
         * 群@所有人
         */
        private Boolean atAll;
    }

}
