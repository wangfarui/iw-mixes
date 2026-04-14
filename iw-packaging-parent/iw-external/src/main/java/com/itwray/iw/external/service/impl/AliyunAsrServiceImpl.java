package com.itwray.iw.external.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.itwray.iw.external.model.dto.AsrSentenceRecognizeDto;
import com.itwray.iw.external.model.enums.ExternalRedisKeyEnum;
import com.itwray.iw.external.model.vo.AsrSentenceRecognizeVo;
import com.itwray.iw.external.service.AsrService;
import com.itwray.iw.web.config.IwAliyunProperties;
import com.itwray.iw.web.exception.IwWebException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 阿里云一句话识别服务实现
 *
 * @author wray
 * @since 2026/4/14
 */
@Service
@Slf4j
public class AliyunAsrServiceImpl implements AsrService {

    private final IwAliyunProperties.Asr aliyunAsrProperties;

    public AliyunAsrServiceImpl(IwAliyunProperties iwAliyunProperties) {
        this.aliyunAsrProperties = iwAliyunProperties.getAsr();
    }

    @Override
    public AsrSentenceRecognizeVo sentenceRecognize(AsrSentenceRecognizeDto dto) {
        this.validateConfig();

        byte[] audioBytes = this.decodeAudioBase64(dto.getAudioBase64());
        String requestUrl = this.buildSentenceRecognizeUrl(dto);
        String token = this.queryToken();

        try (HttpResponse response = HttpUtil.createPost(requestUrl)
                .header("Content-Type", "application/octet-stream")
                .header("Accept", "application/json")
                .header("X-NLS-Token", token)
                .body(audioBytes)
                .timeout(Math.max(
                        this.aliyunAsrProperties.getConnectTimeoutMillis(),
                        this.aliyunAsrProperties.getReadTimeoutMillis()
                ))
                .execute()) {
            String body = response.body();
            if (!response.isOk()) {
                log.error("AliyunAsrService#sentenceRecognize http请求失败, status: {}, body: {}", response.getStatus(), body);
                throw new IwWebException("阿里云语音识别请求失败");
            }
            JSONObject jsonObject = JSONUtil.parseObj(body);
            AsrSentenceRecognizeVo vo = new AsrSentenceRecognizeVo();
            vo.setTaskId(jsonObject.getStr("task_id"));
            vo.setStatus(jsonObject.getInt("status"));
            vo.setMessage(jsonObject.getStr("message"));
            vo.setResult(jsonObject.getStr("result"));
            if (vo != null && !vo.isSuccess()) {
                log.warn("AliyunAsrService#sentenceRecognize 识别失败, status: {}, message: {}, taskId: {}",
                        vo.getStatus(), vo.getMessage(), vo.getTaskId());
            }
            return vo;
        } catch (Exception e) {
            log.error("AliyunAsrService#sentenceRecognize 调用阿里云一句话识别异常", e);
            throw new IwWebException("语音识别异常");
        }
    }

    private void validateConfig() {
        if (this.aliyunAsrProperties == null) {
            throw new IwWebException("阿里云语音识别配置未完成");
        }
        if (StringUtils.isAnyBlank(
                this.aliyunAsrProperties.getAccessKeyId(),
                this.aliyunAsrProperties.getAccessKeySecret(),
                this.aliyunAsrProperties.getAppKey()
        )) {
            throw new IwWebException("阿里云语音识别配置未完成");
        }
    }

    private String buildSentenceRecognizeUrl(AsrSentenceRecognizeDto dto) {
        Map<String, Object> queryMap = new LinkedHashMap<>();
        queryMap.put("appkey", this.aliyunAsrProperties.getAppKey());
        queryMap.put("format", this.resolveFormat(dto));
        queryMap.put("sample_rate", this.resolveSampleRate(dto));
        queryMap.put("enable_punctuation_prediction", this.resolveBooleanValue(
                dto.getEnablePunctuationPrediction(),
                this.aliyunAsrProperties.getEnablePunctuationPrediction()
        ));
        queryMap.put("enable_inverse_text_normalization", this.resolveBooleanValue(
                dto.getEnableInverseTextNormalization(),
                this.aliyunAsrProperties.getEnableInverseTextNormalization()
        ));
        queryMap.put("enable_voice_detection", this.resolveBooleanValue(
                dto.getEnableVoiceDetection(),
                this.aliyunAsrProperties.getEnableVoiceDetection()
        ));
        queryMap.put("disfluency", this.resolveBooleanValue(
                dto.getDisfluency(),
                this.aliyunAsrProperties.getDisfluency()
        ));
        return this.aliyunAsrProperties.getGatewayUrl() + "?" + HttpUtil.toParams(queryMap, StandardCharsets.UTF_8);
    }

    private String resolveFormat(AsrSentenceRecognizeDto dto) {
        return StringUtils.defaultIfBlank(dto.getFormat(), this.aliyunAsrProperties.getDefaultFormat());
    }

    private Integer resolveSampleRate(AsrSentenceRecognizeDto dto) {
        return dto.getSampleRate() == null ? this.aliyunAsrProperties.getDefaultSampleRate() : dto.getSampleRate();
    }

    private String resolveBooleanValue(Boolean requestValue, Boolean defaultValue) {
        return String.valueOf(Boolean.TRUE.equals(requestValue != null ? requestValue : defaultValue));
    }

    private byte[] decodeAudioBase64(String audioBase64) {
        try {
            String normalizedBase64 = audioBase64;
            int base64Index = audioBase64.indexOf("base64,");
            if (base64Index >= 0) {
                normalizedBase64 = audioBase64.substring(base64Index + "base64,".length());
            }
            return Base64.getDecoder().decode(normalizedBase64);
        } catch (Exception e) {
            throw new IwWebException("音频base64数据非法");
        }
    }

    private String queryToken() {
        String cacheToken = ExternalRedisKeyEnum.ALIYUN_ASR_TOKEN.getStringValue(String.class);
        if (StringUtils.isNotBlank(cacheToken)) {
            return cacheToken;
        }

        DefaultProfile profile = DefaultProfile.getProfile(
                this.aliyunAsrProperties.getRegionId(),
                this.aliyunAsrProperties.getAccessKeyId(),
                this.aliyunAsrProperties.getAccessKeySecret()
        );
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain(this.aliyunAsrProperties.getTokenDomain());
        request.setVersion(this.aliyunAsrProperties.getTokenVersion());
        request.setAction(this.aliyunAsrProperties.getTokenAction());
        try {
            CommonResponse response = client.getCommonResponse(request);
            if (response == null || response.getHttpStatus() != 200) {
                log.error("AliyunAsrService#queryToken 获取Token失败, response: {}", response == null ? null : response.getData());
                throw new IwWebException("阿里云语音识别Token获取失败");
            }
            JSONObject jsonObject = JSONUtil.parseObj(response.getData());
            JSONObject tokenObject = jsonObject.getJSONObject("Token");
            if (tokenObject == null || StringUtils.isBlank(tokenObject.getStr("Id"))) {
                log.error("AliyunAsrService#queryToken Token响应异常, response: {}", response.getData());
                throw new IwWebException("阿里云语音识别Token获取失败");
            }

            String token = tokenObject.getStr("Id");
            Long expireTime = tokenObject.getLong("ExpireTime");
            long ttlSeconds = 0L;
            if (expireTime != null) {
                ttlSeconds = expireTime - Instant.now().getEpochSecond() - this.aliyunAsrProperties.getTokenRefreshBeforeSeconds();
            }
            if (ttlSeconds > 0) {
                ExternalRedisKeyEnum.ALIYUN_ASR_TOKEN.setValue(token, ttlSeconds);
            } else {
                ExternalRedisKeyEnum.ALIYUN_ASR_TOKEN.setStringValue(token);
            }
            return token;
        } catch (ClientException e) {
            log.error("AliyunAsrService#queryToken 获取Token异常", e);
            throw new IwWebException("阿里云语音识别Token获取失败");
        }
    }
}
