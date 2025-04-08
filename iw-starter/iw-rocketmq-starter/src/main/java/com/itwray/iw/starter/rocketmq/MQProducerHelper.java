package com.itwray.iw.starter.rocketmq;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.starter.rocketmq.web.RocketMQDataDaoHolder;
import com.itwray.iw.starter.rocketmq.web.dao.BaseMqProduceRecordsDao;
import com.itwray.iw.starter.rocketmq.web.entity.BaseMqProduceRecordsEntity;
import com.itwray.iw.web.model.dto.UserDto;
import com.itwray.iw.web.model.enums.mq.MQDestination;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * MQ生产者助手
 *
 * @author wray
 * @since 2024/10/14
 */
@Slf4j
public abstract class MQProducerHelper {

    private static RocketMQClientTemplate rocketMQClientTemplate;

    private static final ExecutorService executorService = new ThreadPoolExecutor(
            2, // 核心线程数，适当设置避免频繁创建线程
            10, // 最大线程数，控制并发量
            60L, // 非核心线程的存活时间
            TimeUnit.SECONDS, // 存活时间单位
            new LinkedBlockingQueue<>(100), // 队列大小，避免积压过多任务
            Executors.defaultThreadFactory(), // 默认线程工厂
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略，避免丢失任务
    );

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
     * @param destination MQ消息目标
     * @param obj         消息对象
     */
    public static void send(MQDestination destination, Object obj) {
        send(destination.getDestination(), obj);
    }

    /**
     * 同步发送消息
     *
     * @param destination `topicName:tags`
     * @param obj         消息对象
     */
    public static void send(String destination, Object obj) {
        Message<byte[]> message = buildMessage(obj);
        SendReceipt sendReceipt = rocketMQClientTemplate.syncSendNormalMessage(destination, message);
        MQProducerHelper.recordProductionMessages(destination, obj, sendReceipt.getMessageId());
        log.info("MQ消息同步发送成功, messageId: {}", sendReceipt.getMessageId().toString());
    }

    /**
     * 异步发送消息
     *
     * @param destination `topicName:tags`
     * @param obj         消息对象
     */
    public static void asyncSend(String destination, Object obj) {
        Message<byte[]> message = buildMessage(obj);
        CompletableFuture<SendReceipt> completableFuture = rocketMQClientTemplate.asyncSendNormalMessage(destination, message, null);
        completableFuture.thenAccept(sendReceipt -> {
                    MQProducerHelper.recordProductionMessages(destination, obj, sendReceipt.getMessageId());
                    log.info("MQ消息异步发送成功, messageId: {}", sendReceipt.getMessageId().toString());
                })
                .exceptionally(e -> {
                    log.error("MQ消息异步发送失败, obj: " + JSONUtil.toJsonStr(obj), e);
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

    /**
     * 记录生产者消息
     *
     * @param destination `topicName:tags`
     * @param obj         MQ的消息对象
     * @param messageId   发送成功后的消息id
     */
    private static void recordProductionMessages(String destination, Object obj, MessageId messageId) {
        executorService.submit(() -> {
            try {
                String[] tempArr = destination.split(":", 2);
                BaseMqProduceRecordsDao baseMqProduceRecordsDao = RocketMQDataDaoHolder.getBaseMqProduceRecordsDao();
                BaseMqProduceRecordsEntity entity = new BaseMqProduceRecordsEntity();
                entity.setServiceName(RocketMQDataDaoHolder.getApplicationName());
                entity.setMessageId(messageId.toString());
                entity.setVersion(messageId.getVersion());
                entity.setTopic(tempArr[0]);
                entity.setTag(tempArr.length > 1 ? tempArr[1] : "");
                entity.setBody(JSONUtil.toJsonStr(obj));
                entity.setCreateTime(LocalDateTime.now());
                if (obj instanceof UserDto userDto) {
                    entity.setUserId(userDto.getUserId());
                }
                baseMqProduceRecordsDao.save(entity);
            } catch (Exception e) {
                log.error("异步记录生产者消息异常", e);
            }
        });
    }
}
