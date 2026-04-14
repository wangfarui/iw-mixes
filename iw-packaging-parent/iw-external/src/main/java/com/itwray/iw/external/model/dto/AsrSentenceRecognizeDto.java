package com.itwray.iw.external.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 一句话识别请求DTO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "一句话识别请求DTO")
public class AsrSentenceRecognizeDto {

    @NotBlank(message = "音频base64内容不能为空")
    @Schema(title = "音频base64内容")
    private String audioBase64;

    @Schema(title = "音频格式，如 mp3、wav、pcm、amr")
    private String format;

    @Schema(title = "采样率，仅支持8000或16000")
    private Integer sampleRate;

    @Schema(title = "是否启用标点预测")
    private Boolean enablePunctuationPrediction;

    @Schema(title = "是否启用逆文本正规化")
    private Boolean enableInverseTextNormalization;

    @Schema(title = "是否启用语音端点检测")
    private Boolean enableVoiceDetection;

    @Schema(title = "是否过滤语气词")
    private Boolean disfluency;
}
