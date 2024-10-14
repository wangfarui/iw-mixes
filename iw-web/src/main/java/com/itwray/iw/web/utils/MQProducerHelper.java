package com.itwray.iw.web.utils;

import cn.hutool.json.JSONUtil;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * MQ生产者助手
 *
 * @author wray
 * @since 2024/10/14
 */
public abstract class MQProducerHelper {

    private static RocketMQClientTemplate rocketMQClientTemplate;

    /**
     * 初始化RocketMQClientTemplate
     *
     * @param rocketMQClientTemplate RocketMQClientTemplate
     */
    public static void setRocketMQClientTemplate(RocketMQClientTemplate rocketMQClientTemplate) {
        MQProducerHelper.rocketMQClientTemplate = rocketMQClientTemplate;
    }

    /**
     * 同步发送消息
     *
     * @param topic Topic
     * @param obj   消息对象
     */
    public static void send(String topic, Object obj) {
        Message<byte[]> message = buildMessage(obj);
        rocketMQClientTemplate.send(topic, message);
        System.out.println("消息同步发送成功. topic: " + topic);
    }

    /**
     * 异步发送消息
     *
     * @param topic Topic
     * @param obj   消息对象
     */
    public static void asyncSend(String topic, Object obj) {
        Message<byte[]> message = buildMessage(obj);
        CompletableFuture<SendReceipt> completableFuture = rocketMQClientTemplate.asyncSendNormalMessage(topic, message, null);
        completableFuture.thenAccept(sendReceipt -> System.out.println("消息发送成功: " + sendReceipt.getMessageId()))
                .exceptionally(e -> {
                    System.out.println("消息发送异常, obj: " + obj);
                    e.printStackTrace();
                    return null;
                });
    }

    /**
     * 构建消息体
     *
     * @param obj 消息对象
     * @return Message
     */
    public static Message<byte[]> buildMessage(Object obj) {
        Assert.notNull(rocketMQClientTemplate, "RocketMQClientTemplate is null");
        byte[] bytes = JSONUtil.toJsonStr(obj).getBytes(StandardCharsets.UTF_8);
        return MessageBuilder.withPayload(bytes).build();
    }
}
