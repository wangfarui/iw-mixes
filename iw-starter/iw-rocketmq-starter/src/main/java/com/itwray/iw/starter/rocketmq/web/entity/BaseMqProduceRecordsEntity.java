package com.itwray.iw.starter.rocketmq.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itwray.iw.web.model.entity.IdEntity;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * MQ消息生产记录表
 *
 * @author wray
 * @since 2025-02-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_mq_produce_records")
public class BaseMqProduceRecordsEntity extends IdEntity<Long> {

    /**
     * 消息生产记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息版本
     */
    private String version;

    /**
     * 消息topic
     */
    private String topic;

    /**
     * 消息tag
     */
    private String tag;

    /**
     * 消息体
     */
    private String body;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    @Nullable
    private Integer userId;
}
