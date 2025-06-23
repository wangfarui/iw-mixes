package com.itwray.iw.external.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.external.model.bo.AIMessage;
import com.itwray.iw.external.model.bo.AIRequestBody;
import com.itwray.iw.external.model.bo.AIResponseBody;
import com.itwray.iw.external.model.bo.AIResponseFormat;
import com.itwray.iw.external.model.enums.ExternalRedisKeyEnum;
import com.itwray.iw.external.service.AIService;
import com.itwray.iw.starter.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * AI 服务实现层
 *
 * @author farui.wang
 * @since 2025/6/19
 */
@Service
@Slf4j
public class AIServiceImpl implements AIService {

    @Value("${iw.ai.api-url:https://api.deepseek.com/chat/completions}")
    private String apiUrl;

    @Value("${iw.ai.api-key}")
    private String apiKey;

    @Override
    public String answer(String content) {
        if (StringUtils.isBlank(content)) {
            return "请输入您的对话内容";
        }
        List<AIMessage> messages = new ArrayList<>();
        messages.add(new AIMessage("你是一个百科助手,针对用户提问,可以精准且简要的回复问题.", "system"));
        messages.add(new AIMessage(content, "user"));

        AIRequestBody requestBody = new AIRequestBody();
        requestBody.setMessages(messages);
        requestBody.setModel("deepseek-chat");
        requestBody.setFrequency_penalty(0);
        requestBody.setMax_tokens(1024);
        requestBody.setPresence_penalty(0);
        requestBody.setResponse_format(new AIResponseFormat("text"));
        requestBody.setStream(false);
        requestBody.setTemperature(new BigDecimal("1.3"));
        requestBody.setTop_p(new BigDecimal("1"));
        requestBody.setLogprobs(false);

        try (HttpResponse response = HttpUtil.createPost(this.apiUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", this.apiKey)
                .body(JSONUtil.toJsonStr(requestBody))
                .execute()) {
            if (!response.isOk()) {
                return "服务请求失败, 请重试";
            }
            AIResponseBody responseBody = JSONUtil.toBean(response.body(), AIResponseBody.class);
            if (responseBody != null && CollUtil.isNotEmpty(responseBody.getChoices())) {
                AIResponseBody.Choice choice = responseBody.getChoices().get(0);
                return choice.getMessage().getContent();
            }
        }

        return "服务请求超时, 请重试";
    }

    @Override
    public String chat(String content) {
        List<AIMessage> messages = new ArrayList<>();
        messages.add(new AIMessage(content, "user"));

        AIRequestBody body = new AIRequestBody();
        body.setMessages(messages);
        body.setModel("deepseek-chat");
        body.setFrequency_penalty(0);
        body.setMax_tokens(1024);
        body.setPresence_penalty(0);
        body.setResponse_format(new AIResponseFormat("text"));
        body.setStream(true);
        body.setTemperature(new BigDecimal("1.0"));
        body.setTop_p(new BigDecimal("1"));
        body.setLogprobs(false);

        HttpRequest httpRequest = HttpUtil.createPost(this.apiUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "text/event-stream") // 声明接收流式数据
                .header("Authorization", this.apiKey)
                .body(JSONUtil.toJsonStr(body))
                // 设置长超时
                .timeout(60 * 1000);

        // 使用 StringBuilder 拼接完整内容
        StringBuilder fullContent = new StringBuilder();
        try (HttpResponse response = httpRequest.executeAsync()) {
            Scanner scanner = new Scanner(response.bodyStream(), StandardCharsets.UTF_8);

            // 逐行读取流式数据
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("data:") && !line.equals("data: [DONE]")) {
                    String jsonStr = line.substring(5).trim();
                    if (JSONUtil.isTypeJSON(jsonStr)) {
                        JSONObject json = JSONUtil.parseObj(jsonStr);
                        if (json.containsKey("choices")) {
                            String chunkContent = json.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("delta")
                                    .getStr("content", "");
                            fullContent.append(chunkContent);
                            System.out.print(chunkContent); // 实时打印到控制台
                        }

                        // 检查是否因长度截断
                        String finishReason = json.getJSONArray("choices")
                                .getJSONObject(0)
                                .getStr("finish_reason");
                        if ("length".equals(finishReason)) {
                            System.out.println("\n警告：回答因长度限制可能不完整！");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();
        }

        return fullContent.toString();
    }

    @Override
    public HttpResponse streamChat(@NonNull String chatId, @NonNull String prompt) {
        log.info("收到对话: chatId: {}, prompt: {}", chatId, prompt);
        List<AIMessage> messageList = new ArrayList<>();
        // 获取之前的对话内容
        Set<AIMessage> messageSet = RedisUtil.members(ExternalRedisKeyEnum.AI_CHAT_CONTENT.getKey(chatId), AIMessage.class);
        if (CollUtil.isEmpty(messageSet)) {
            AIMessage firstMessage = new AIMessage("你是一个百科助手,针对用户提问,可以精准且简要的回复问题.", "system");
            Long sortValue = RedisUtil.incrementOne(ExternalRedisKeyEnum.AI_CHAT_SORT.getKey(chatId));
            firstMessage.setInnerSort(sortValue);
            // 添加到AI消息体和缓存中
            messageList.add(firstMessage);
            RedisUtil.sSetJson(ExternalRedisKeyEnum.AI_CHAT_CONTENT.getKey(chatId), firstMessage);
        } else {
            messageList.addAll(messageSet);
        }
        // 构建当前对话内容
        AIMessage userMessage = new AIMessage(prompt, "user");
        Long sortValue = RedisUtil.incrementOne(ExternalRedisKeyEnum.AI_CHAT_SORT.getKey(chatId));
        userMessage.setInnerSort(sortValue);
        // 添加到AI消息体和缓存中
        messageList.add(userMessage);
        RedisUtil.sSetJson(ExternalRedisKeyEnum.AI_CHAT_CONTENT.getKey(chatId), userMessage);
        // 更新对话内容的缓存时间
        ExternalRedisKeyEnum.AI_CHAT_CONTENT.setExpire(chatId);
        ExternalRedisKeyEnum.AI_CHAT_SORT.setExpire(chatId);

        // 构建AI请求体
        AIRequestBody requestBody = new AIRequestBody();
        requestBody.setMessages(messageList.stream().sorted(Comparator.comparing(AIMessage::getInnerSort)).toList());
        requestBody.setModel("deepseek-chat");
        requestBody.setFrequency_penalty(0);
        requestBody.setMax_tokens(1024);
        requestBody.setPresence_penalty(0);
        requestBody.setResponse_format(new AIResponseFormat("text"));
        requestBody.setStream(true);
        requestBody.setTemperature(new BigDecimal("1.3"));
        requestBody.setTop_p(new BigDecimal("1"));
        requestBody.setLogprobs(false);

        return HttpUtil.createPost(this.apiUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "text/event-stream")
                .header("Authorization", this.apiKey)
                .body(JSONUtil.toJsonStr(requestBody))
                .timeout(60 * 1000)
                .executeAsync();
    }
}
