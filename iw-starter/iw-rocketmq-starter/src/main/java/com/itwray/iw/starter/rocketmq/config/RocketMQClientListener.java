package com.itwray.iw.starter.rocketmq.config;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.web.model.dto.UserDto;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * RocketMQ客户端监听器
 * <p>需配合{@link RocketMQMessageListener}使用</p>
 *
 * @author wray
 * @since 2024/10/14
 */
public interface RocketMQClientListener<T extends UserDto> extends RocketMQListener {

    /**
     * 消费实例接收的参数类型
     *
     * @return doConsume方法参数
     */
    Class<T> getGenericClass();

    void doConsume(T t);

    @Override
    default ConsumeResult consume(MessageView messageView) {
        // 从 MessageView 中获取 ByteBuffer
        ByteBuffer byteBuffer = messageView.getBody();

        // 转换 ByteBuffer 为字节数组
        byte[] body = new byte[byteBuffer.remaining()];
        byteBuffer.get(body);

        // 处理字节数组，例如转换为字符串
        String messageBody = new String(body, StandardCharsets.UTF_8);

        // 转换JSON字符串为Object对象
        T t = JSONUtil.toBean(messageBody, getGenericClass());
        if (t.getUserId() != null) {
            UserUtils.setUserId(t.getUserId());
        }

        // 执行业务消费逻辑
        try {
            doConsume(t);
            return ConsumeResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeResult.FAILURE;
        } finally {
            UserUtils.removeUserId();
        }
    }
}
