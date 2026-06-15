package com.itwray.iw.bookkeeping.service.impl;

import com.itwray.iw.bookkeeping.service.BookkeepingAssistantRemoteService;
import com.itwray.iw.external.client.InternalApiClient;
import com.itwray.iw.external.model.dto.AiStructuredChatDto;
import com.itwray.iw.external.model.dto.AsrSentenceRecognizeDto;
import com.itwray.iw.external.model.vo.AiStructuredChatVo;
import com.itwray.iw.external.model.vo.AsrSentenceRecognizeVo;
import com.itwray.iw.web.exception.IwWebException;
import org.springframework.stereotype.Service;

/**
 * 记账助手远程能力服务实现
 *
 * @author wray
 * @since 2026/4/14
 */
@Service
public class BookkeepingAssistantRemoteServiceImpl implements BookkeepingAssistantRemoteService {

    private final InternalApiClient internalApiClient;

    public BookkeepingAssistantRemoteServiceImpl(InternalApiClient internalApiClient) {
        this.internalApiClient = internalApiClient;
    }

    @Override
    public AsrSentenceRecognizeVo sentenceRecognition(AsrSentenceRecognizeDto dto) {
        return this.requireResponse(internalApiClient.sentenceRecognition(dto), "调用语音识别服务失败");
    }

    @Override
    public AiStructuredChatVo structuredChat(AiStructuredChatDto dto) {
        return this.requireResponse(internalApiClient.structuredChat(dto), "调用AI解析服务失败");
    }

    private <T> T requireResponse(T response, String defaultMessage) {
        if (response == null) {
            throw new IwWebException(defaultMessage);
        }
        return response;
    }
}
