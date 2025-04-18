package com.itwray.iw.web.model.enums.mq;

import com.itwray.iw.web.constants.MQTopicConstants;
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

    PointsRecordsTopicEnum(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }


    @Override
    public String getTopic() {
        return MQTopicConstants.POINTS_RECORDS;
    }

    @Override
    public String getDestination() {
        return MQTopicConstants.POINTS_RECORDS + ":" + getTag();
    }
}
