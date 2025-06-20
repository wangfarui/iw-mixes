package com.itwray.iw.external.handler;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.external.service.AIService;
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
        log.info("WebSocket连接建立: {}", session.getUri());
        // 1. 获取握手信息中的token
        String token = session.getHandshakeHeaders().getFirst(RequestHeaderConstants.TOKEN_HEADER);

        // 2. 验证token逻辑
        if (!isValidToken(token)) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Unauthorized"));
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String prompt = message.getPayload();
        if (StringUtils.isBlank(prompt)) {
            session.sendMessage(new TextMessage("请输入您的对话内容"));
            return;
        }

        // 流式读取并转发给WebSocket客户端
        try (HttpResponse httpResponse = aiService.streamChat(prompt);
             BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.bodyStream()))) {
            String line;
            while ((line = reader.readLine()) != null && session.isOpen()) {
                if (line.startsWith("data:")) {
                    String json = line.substring(5).trim();
                    String content = extractContent(json);
                    if (content != null) {
                        session.sendMessage(new TextMessage(content));
                    }
                }
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("服务调用失败: " + e.getMessage()));
            session.close();
            log.error("服务调用失败: " + e.getMessage(), e);
        }
    }

    private String extractContent(String json) {
        if (json == null) {
            return null;
        }
        if (json.startsWith("[DONE]")) {
            return null;
        }
        return JSONUtil.parseObj(json)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("delta")
                .getStr("content");
    }

    private boolean isValidToken(String token) {
        // 实现你的token验证逻辑
        // 示例: 检查token是否有效
        return token != null && token.startsWith("Bearer ");
    }
}