package com.itwray.iw.web.model.enums.mq;

import lombok.Getter;

/**
 * 记账记录 Topic
 *
 * @author wray
 * @since 2025/4/18
 */
@Getter
public enum BookkeepingRecordsTopicEnum implements MQDestination {

    WALLET_BALANCE("wallet_balance", "钱包余额")
    ;

    private final String tag;

    private final String name;

    public static final String TOPIC = "bookkeeping_records";

    BookkeepingRecordsTopicEnum(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }


    @Override
    public String getTopic() {
        return TOPIC;
    }

    @Override
    public String getDestination() {
        return TOPIC + ":" + getTag();
    }
}
