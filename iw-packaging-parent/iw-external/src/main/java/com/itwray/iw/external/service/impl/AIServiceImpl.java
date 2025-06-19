package com.itwray.iw.external.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.external.model.bo.AIRequestBody;
import com.itwray.iw.external.model.bo.AIMessage;
import com.itwray.iw.external.model.bo.AIResponseBody;
import com.itwray.iw.external.model.bo.AIResponseFormat;
import com.itwray.iw.external.service.AIService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AI 服务实现层
 *
 * @author farui.wang
 * @since 2025/6/19
 */
@Service
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

            System.out.println("\n完整内容：\n" + fullContent);
        } catch (Exception e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();
        }

        return fullContent.toString();
    }
}
