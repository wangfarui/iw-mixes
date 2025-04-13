package com.itwray.iw.web.model.enums.mq;

/**
 * MQ消息目标
 *
 * @author wray
 * @since 2025/4/8
 */
public interface MQDestination {

    /**
     * 获取Topic名称
     */
    String getTopic();

    /**
     * 获取标签名称
     */
    String getTag();

    /**
     * 获取消息目标名称
     *
     * @return formats: `topicName:tags`
     */
    String getDestination();
}
