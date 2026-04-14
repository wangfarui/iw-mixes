package com.itwray.iw.external.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一句话识别响应VO
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@Schema(name = "一句话识别响应VO")
public class AsrSentenceRecognizeVo {

    @Schema(title = "任务ID")
    private String taskId;

    @Schema(title = "阿里云返回状态码")
    private Integer status;

    @Schema(title = "阿里云返回消息")
    private String message;

    @Schema(title = "识别文本")
    private String result;

    public boolean isSuccess() {
        return Integer.valueOf(20000000).equals(this.status);
    }
}
