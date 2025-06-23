package com.itwray.iw.external.handler;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.external.model.bo.AIMessage;
import com.itwray.iw.external.model.enums.ExternalRedisKeyEnum;
import com.itwray.iw.external.service.AIService;
import com.itwray.iw.starter.redis.RedisUtil;
import com.itwray.iw.web.client.AuthenticationClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class DeepSeekWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private AIService aiService;

    @Autowired
    private AuthenticationClient authenticationClient;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        log.info("WebSocket连接建立: id: {}, uri: {}", session.getId(), session.getUri());
        // 1. 获取握手信息中的token
        String token = session.getHandshakeHeaders().getFirst(RequestHeaderConstants.TOKEN_HEADER);

        // 2. 验证token逻辑
        if (!isValidToken(token)) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Unauthorized"));
        }

        // 给当前会话 初始化一个内容排序
        RedisUtil.incrementOne(ExternalRedisKeyEnum.AI_CHAT_SORT.getKey(session.getId()));
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String prompt = message.getPayload();
        if (StringUtils.isBlank(prompt)) {
            session.sendMessage(new TextMessage("请输入您的对话内容"));
            return;
        }

        // 流式读取并转发给WebSocket客户端
        try (HttpResponse httpResponse = aiService.streamChat(session.getId(), prompt);
             BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.bodyStream()))) {
            StringBuilder fullContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null && session.isOpen()) {
                if (line.startsWith("data:")) {
                    String json = line.substring(5).trim();
                    if (!json.startsWith("[DONE]")) {
                        String content = extractContent(json);
                        if (StringUtils.isNotBlank(content)) {
                            fullContent.append(content);
                            session.sendMessage(new TextMessage(content));
                        }
                    }
                }
            }
            AIMessage assistantMessage = new AIMessage(fullContent.toString(), "assistant");
            Long sortValue = RedisUtil.incrementOne(ExternalRedisKeyEnum.AI_CHAT_SORT.getKey(session.getId()));
            assistantMessage.setInnerSort(sortValue);
            RedisUtil.sSetJson(ExternalRedisKeyEnum.AI_CHAT_CONTENT.getKey(session.getId()), assistantMessage);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("服务调用失败: " + e.getMessage()));
            session.close();
            log.error("服务调用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket断开建立: id: {}, uri: {}", session.getId(), session.getUri());
        RedisUtil.delete(ExternalRedisKeyEnum.AI_CHAT_CONTENT.getKey(session.getId()));
        super.afterConnectionClosed(session, status);
    }

    private String extractContent(String json) {
        return JSONUtil.parseObj(json)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("delta")
                .getStr("content");
    }

    private boolean isValidToken(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        Boolean res = authenticationClient.validateToken(token);
        return res != null && res;
    }
}