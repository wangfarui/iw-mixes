package com.itwray.iw.bookkeeping.service;

import com.itwray.iw.external.model.dto.AiStructuredChatDto;
import com.itwray.iw.external.model.dto.AsrSentenceRecognizeDto;
import com.itwray.iw.external.model.vo.AiStructuredChatVo;
import com.itwray.iw.external.model.vo.AsrSentenceRecognizeVo;

/**
 * 记账助手远程能力服务
 *
 * @author wray
 * @since 2026/4/14
 */
public interface BookkeepingAssistantRemoteService {

    /**
     * 一句话语音识别
     *
     * @param dto 识别参数
     * @return 识别结果
     */
    AsrSentenceRecognizeVo sentenceRecognition(AsrSentenceRecognizeDto dto);

    /**
     * 结构化AI对话
     *
     * @param dto 对话参数
     * @return 对话结果
     */
    AiStructuredChatVo structuredChat(AiStructuredChatDto dto);
}
