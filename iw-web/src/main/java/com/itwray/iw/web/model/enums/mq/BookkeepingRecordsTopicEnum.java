package com.itwray.iw.web.model.enums.mq;

import com.itwray.iw.web.constants.MQTopicConstants;
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

    BookkeepingRecordsTopicEnum(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }


    @Override
    public String getTopic() {
        return MQTopicConstants.BOOKKEEPING_RECORDS;
    }

    @Override
    public String getDestination() {
        return MQTopicConstants.BOOKKEEPING_RECORDS + ":" + getTag();
    }
}
