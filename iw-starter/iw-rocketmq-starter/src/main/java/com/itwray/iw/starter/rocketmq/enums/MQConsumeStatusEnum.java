package com.itwray.iw.starter.rocketmq.enums;

import com.itwray.iw.web.model.enums.BusinessConstantEnum;
import lombok.Getter;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;

/**
 * MQ 消息消费状态枚举
 *
 * @author wray
 * @since 2025/2/10
 */
@Getter
public enum MQConsumeStatusEnum implements BusinessConstantEnum {

    WAIT(0, "待消费"),
    SUCCESS(1, "消费成功"),
    FAILURE(2, "消费失败");

    private final Integer code;

    private final String name;

    MQConsumeStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MQConsumeStatusEnum of(ConsumeResult consumeResult) {
        switch (consumeResult) {
            case SUCCESS -> {
                return MQConsumeStatusEnum.SUCCESS;
            }
            case FAILURE -> {
                return MQConsumeStatusEnum.FAILURE;
            }
        }
        return MQConsumeStatusEnum.WAIT;
    }
}
