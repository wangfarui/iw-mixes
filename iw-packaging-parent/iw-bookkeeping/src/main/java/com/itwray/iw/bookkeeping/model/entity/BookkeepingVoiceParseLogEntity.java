package com.itwray.iw.bookkeeping.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 语音记账解析日志表
 *
 * @author wray
 * @since 2026/4/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookkeeping_voice_parse_log")
public class BookkeepingVoiceParseLogEntity extends UserEntity<Integer> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 解析状态
     */
    private String parseStatus;

    /**
     * 确认状态
     */
    private String confirmStatus;

    /**
     * 原始识别文本
     */
    private String recognizedText;

    /**
     * 音频格式
     */
    private String audioFormat;

    /**
     * 音频时长(毫秒)
     */
    private Integer audioDurationMs;

    /**
     * 解析置信度
     */
    private BigDecimal confidence;

    /**
     * 匹配的记账行为ID
     */
    private Integer matchedActionId;

    /**
     * 确认生成的记账记录ID
     */
    private Integer confirmedRecordId;

    /**
     * 解析结果草稿JSON
     */
    private String draftJson;

    /**
     * 解析警告JSON
     */
    private String warningJson;

    /**
     * 确认提交数据JSON
     */
    private String confirmedDataJson;

    /**
     * AI原始响应
     */
    private String aiRawResponse;

    /**
     * 服务提供方
     */
    private String provider;

    /**
     * 确认时间
     */
    private LocalDateTime confirmedTime;
}
