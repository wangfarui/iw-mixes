package com.itwray.iw.web.model.enums.mq;

import lombok.Getter;

/**
 * 积分记录 Topic
 *
 * @author wray
 * @since 2025/4/18
 */
@Getter
public enum PointsRecordsTopicEnum implements MQDestination {

    TASK("task", "积分任务"),
    EXCITATION_BOOKKEEPING("excitation_bookkeeping", "记账收入激励"),
    ;

    private final String tag;

    private final String name;

    public static final String TOPIC = "points-records";

    PointsRecordsTopicEnum(String tag, String name) {
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
