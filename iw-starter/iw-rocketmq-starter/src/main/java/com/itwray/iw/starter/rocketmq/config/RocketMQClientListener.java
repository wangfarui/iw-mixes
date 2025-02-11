package com.itwray.iw.starter.rocketmq.config;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.starter.rocketmq.enums.MQConsumeStatusEnum;
import com.itwray.iw.starter.rocketmq.web.RocketMQConsumeDaoHolder;
import com.itwray.iw.starter.rocketmq.web.dao.BaseMqConsumeRecordsDao;
import com.itwray.iw.starter.rocketmq.web.entity.BaseMqConsumeRecordsEntity;
import com.itwray.iw.web.model.dto.UserDto;
import com.itwray.iw.web.utils.UserUtils;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageId;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * RocketMQ客户端监听器
 * <p>需配合{@link RocketMQMessageListener}使用</p>
 *
 * @author wray
 * @since 2024/10/14
 */
public interface RocketMQClientListener<T> extends RocketMQListener {

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
        if (t instanceof UserDto userDto) {
            UserUtils.setUserId(userDto.getUserId());
        }

        // 新增消费记录
        Long consumeRecordId = addConsumeRecord(messageView, t);

        // 执行业务消费逻辑
        ConsumeResult consumeResult = null;
        try {
            doConsume(t);
            consumeResult = ConsumeResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            consumeResult = ConsumeResult.FAILURE;
        } finally {
            updateConsumeStatus(consumeRecordId, consumeResult);
            UserUtils.removeUserId();
        }
        return consumeResult;
    }

    default Long addConsumeRecord(MessageView messageView, T body) {
        BaseMqConsumeRecordsDao baseMqConsumeRecordsDao = RocketMQConsumeDaoHolder.getBaseMqConsumeRecordsDao();
        if (baseMqConsumeRecordsDao == null) {
            return null;
        }
        BaseMqConsumeRecordsEntity entity = new BaseMqConsumeRecordsEntity();
        entity.setServiceName(RocketMQConsumeDaoHolder.getApplicationName());
        MessageId messageId = messageView.getMessageId();
        entity.setMessageId(messageId.toString());
        entity.setVersion(messageId.getVersion());
        entity.setTopic(messageView.getTopic());
        if (messageView.getTag().isPresent()) {
            entity.setTag(messageView.getTag().get());
        }
        entity.setBody(JSONUtil.toJsonStr(body));
        entity.setStatus(MQConsumeStatusEnum.WAIT);
        entity.setCreateTime(LocalDateTime.now());
        if (body instanceof UserDto userDto) {
            entity.setUserId(userDto.getUserId());
        }
        baseMqConsumeRecordsDao.save(entity);
        return entity.getId();
    }

    default void updateConsumeStatus(Long id, ConsumeResult consumeResult) {
        if (id == null) return;
        BaseMqConsumeRecordsDao baseMqConsumeRecordsDao = RocketMQConsumeDaoHolder.getBaseMqConsumeRecordsDao();
        if (baseMqConsumeRecordsDao == null) {
            return;
        }
        baseMqConsumeRecordsDao.lambdaUpdate()
                .eq(BaseMqConsumeRecordsEntity::getId, id)
                .set(BaseMqConsumeRecordsEntity::getStatus, MQConsumeStatusEnum.of(consumeResult))
                .update();
    }
}
