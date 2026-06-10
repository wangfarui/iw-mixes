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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        ResolvedAudioInfo resolvedAudioInfo = this.resolveAudioInfo(audioBytes, dto);
        String requestUrl = this.buildSentenceRecognizeUrl(resolvedAudioInfo, dto);
        String token = this.queryToken();

        try (HttpResponse response = HttpUtil.createPost(requestUrl)
                .header("Content-Type", "application/octet-stream")
                .header("Accept", "application/json")
                .header("X-NLS-Token", token)
                .body(resolvedAudioInfo.getAudioBytes())
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
            if (!vo.isSuccess()) {
                log.warn("AliyunAsrService#sentenceRecognize 识别失败, status: {}, message: {}, taskId: {}, resolvedFormat: {}, resolvedSampleRate: {}, audioSize: {}",
                        vo.getStatus(), vo.getMessage(), vo.getTaskId(),
                        resolvedAudioInfo.getFormat(), resolvedAudioInfo.getSampleRate(), resolvedAudioInfo.getAudioBytes().length);
                throw new IwWebException("阿里云语音识别失败: " + StringUtils.defaultIfBlank(vo.getMessage(), "UNKNOWN_ERROR"));
            }
            if (StringUtils.isBlank(vo.getResult())) {
                log.warn("AliyunAsrService#sentenceRecognize 识别成功但结果为空, taskId: {}, declaredFormat: {}, declaredSampleRate: {}, resolvedFormat: {}, resolvedSampleRate: {}, audioSize: {}, audioHeaderHex: {}",
                        vo.getTaskId(),
                        dto.getFormat(),
                        dto.getSampleRate(),
                        resolvedAudioInfo.getFormat(),
                        resolvedAudioInfo.getSampleRate(),
                        resolvedAudioInfo.getAudioBytes().length,
                        this.buildAudioHeaderHex(resolvedAudioInfo.getAudioBytes()));
            }
            return vo;
        } catch (IwWebException e) {
            throw e;
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

    private String buildSentenceRecognizeUrl(ResolvedAudioInfo resolvedAudioInfo, AsrSentenceRecognizeDto dto) {
        Map<String, Object> queryMap = new LinkedHashMap<>();
        queryMap.put("appkey", this.aliyunAsrProperties.getAppKey());
        queryMap.put("format", resolvedAudioInfo.getFormat());
        queryMap.put("sample_rate", resolvedAudioInfo.getSampleRate());
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
        return StringUtils.lowerCase(StringUtils.defaultIfBlank(dto.getFormat(), this.aliyunAsrProperties.getDefaultFormat()));
    }

    private Integer resolveSampleRate(AsrSentenceRecognizeDto dto) {
        return dto.getSampleRate() == null ? this.aliyunAsrProperties.getDefaultSampleRate() : dto.getSampleRate();
    }

    private ResolvedAudioInfo resolveAudioInfo(byte[] audioBytes, AsrSentenceRecognizeDto dto) {
        String resolvedFormat = this.resolveFormat(dto);
        Integer resolvedSampleRate = this.resolveSampleRate(dto);
        byte[] resolvedAudioBytes = audioBytes;

        if (this.isWebmAudio(audioBytes)) {
            log.info("AliyunAsrService#sentenceRecognize 检测到WebM音频，准备转码, declaredFormat: {}, declaredSampleRate: {}, audioHeaderHex: {}",
                    dto.getFormat(), dto.getSampleRate(), this.buildAudioHeaderHex(audioBytes));
            resolvedAudioBytes = this.transcodeWebmToWave(audioBytes);
            resolvedFormat = "wav";
            resolvedSampleRate = 16000;
        }

        if (this.isWaveAudio(resolvedAudioBytes)) {
            Integer wavSampleRate = this.parseWaveSampleRate(resolvedAudioBytes);
            if (!"wav".equals(resolvedFormat) || !Objects.equals(wavSampleRate, resolvedSampleRate)) {
                log.info("AliyunAsrService#sentenceRecognize 检测到WAV音频头, declaredFormat: {}, declaredSampleRate: {}, resolvedSampleRate: {}",
                        dto.getFormat(), dto.getSampleRate(), wavSampleRate);
            }
            resolvedFormat = "wav";
            if (wavSampleRate != null) {
                resolvedSampleRate = wavSampleRate;
            }
        } else if (this.isAmrAudio(resolvedAudioBytes)) {
            resolvedFormat = "amr";
        } else if (this.isMp3Audio(resolvedAudioBytes)) {
            resolvedFormat = "mp3";
        }

        ResolvedAudioInfo resolvedAudioInfo = new ResolvedAudioInfo();
        resolvedAudioInfo.setAudioBytes(resolvedAudioBytes);
        resolvedAudioInfo.setFormat(resolvedFormat);
        resolvedAudioInfo.setSampleRate(resolvedSampleRate);
        return resolvedAudioInfo;
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

    private boolean isWaveAudio(byte[] audioBytes) {
        return audioBytes != null
                && audioBytes.length >= 12
                && audioBytes[0] == 'R'
                && audioBytes[1] == 'I'
                && audioBytes[2] == 'F'
                && audioBytes[3] == 'F'
                && audioBytes[8] == 'W'
                && audioBytes[9] == 'A'
                && audioBytes[10] == 'V'
                && audioBytes[11] == 'E';
    }

    private boolean isWebmAudio(byte[] audioBytes) {
        return audioBytes != null
                && audioBytes.length >= 4
                && (audioBytes[0] & 0xFF) == 0x1A
                && (audioBytes[1] & 0xFF) == 0x45
                && (audioBytes[2] & 0xFF) == 0xDF
                && (audioBytes[3] & 0xFF) == 0xA3;
    }

    private boolean isAmrAudio(byte[] audioBytes) {
        return audioBytes != null
                && audioBytes.length >= 6
                && audioBytes[0] == '#'
                && audioBytes[1] == '!'
                && audioBytes[2] == 'A'
                && audioBytes[3] == 'M'
                && audioBytes[4] == 'R'
                && audioBytes[5] == '\n';
    }

    private boolean isMp3Audio(byte[] audioBytes) {
        return audioBytes != null
                && audioBytes.length >= 3
                && ((audioBytes[0] == 'I' && audioBytes[1] == 'D' && audioBytes[2] == '3')
                || ((audioBytes[0] & 0xFF) == 0xFF && (audioBytes[1] & 0xE0) == 0xE0));
    }

    private Integer parseWaveSampleRate(byte[] audioBytes) {
        if (!this.isWaveAudio(audioBytes) || audioBytes.length < 28) {
            return null;
        }
        return (audioBytes[24] & 0xFF)
                | ((audioBytes[25] & 0xFF) << 8)
                | ((audioBytes[26] & 0xFF) << 16)
                | ((audioBytes[27] & 0xFF) << 24);
    }

    private String buildAudioHeaderHex(byte[] audioBytes) {
        if (audioBytes == null || audioBytes.length == 0) {
            return "";
        }
        byte[] headerBytes = Arrays.copyOf(audioBytes, Math.min(audioBytes.length, 16));
        StringBuilder builder = new StringBuilder();
        for (byte headerByte : headerBytes) {
            builder.append(String.format("%02X", headerByte));
        }
        return builder.toString();
    }

    private byte[] transcodeWebmToWave(byte[] webmBytes) {
        Path inputPath = null;
        Path outputPath = null;
        try {
            inputPath = Files.createTempFile("iw-asr-source-", ".webm");
            outputPath = Files.createTempFile("iw-asr-target-", ".wav");
            Files.write(inputPath, webmBytes);

            ProcessBuilder processBuilder = new ProcessBuilder(
                    this.aliyunAsrProperties.getFfmpegCommand(),
                    "-y",
                    "-i", inputPath.toString(),
                    "-vn",
                    "-acodec", "pcm_s16le",
                    "-ac", "1",
                    "-ar", "16000",
                    outputPath.toString()
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            String ffmpegOutput;
            try (InputStream inputStream = process.getInputStream()) {
                boolean finished = process.waitFor(this.aliyunAsrProperties.getFfmpegTimeoutSeconds(), TimeUnit.SECONDS);
                ffmpegOutput = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!finished) {
                    process.destroyForcibly();
                    log.error("AliyunAsrService#transcodeWebmToWave 转码超时, ffmpegOutput: {}", ffmpegOutput);
                    throw new IwWebException("WebM音频转码超时，请检查ffmpeg配置");
                }
            }

            if (process.exitValue() != 0 || !Files.exists(outputPath) || Files.size(outputPath) <= 0) {
                log.error("AliyunAsrService#transcodeWebmToWave 转码失败, exitCode: {}, ffmpegOutput: {}",
                        process.exitValue(), ffmpegOutput);
                throw new IwWebException("WebM音频转码失败，请检查ffmpeg配置");
            }

            byte[] wavBytes = Files.readAllBytes(outputPath);
            log.info("AliyunAsrService#transcodeWebmToWave 转码成功, sourceSize: {}, targetSize: {}, ffmpegCommand: {}",
                    webmBytes.length, wavBytes.length, this.aliyunAsrProperties.getFfmpegCommand());
            return wavBytes;
        } catch (IwWebException e) {
            throw e;
        } catch (IOException e) {
            log.error("AliyunAsrService#transcodeWebmToWave 转码IO异常", e);
            throw new IwWebException("WebM音频转码失败，请检查ffmpeg配置");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IwWebException("WebM音频转码被中断");
        } finally {
            this.deleteTempFile(inputPath);
            this.deleteTempFile(outputPath);
        }
    }

    private void deleteTempFile(Path filePath) {
        if (filePath == null) {
            return;
        }
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("AliyunAsrService#deleteTempFile 删除临时文件失败, filePath: {}", filePath, e);
        }
    }

    @lombok.Data
    private static class ResolvedAudioInfo {

        private byte[] audioBytes;

        private String format;

        private Integer sampleRate;
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
