package com.itwray.iw.external.service;

import cn.hutool.http.HttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * AI 接口服务
 *
 * @author farui.wang
 * @since 2025/6/19
 */
public interface AIService {

    String answer(String content);

    String chat(String content);

    /**
     * 流式对话
     * @param chatId 对话id
     * @param prompt 当前对话内容
     * @return AI回答内容
     */
    HttpResponse streamChat(@NonNull String chatId, @NonNull String prompt) throws IOException;
}
