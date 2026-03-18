package com.itwray.iw.web.model.enums.mq;

import lombok.Getter;

/**
 * 家庭组 Topic
 *
 * @author wray
 * @since 2026/3/12
 */
@Getter
public enum FamilyGroupTopicEnum implements MQDestination {

    MEMBER_LEAVE("member_leave", "成员离组");

    private final String tag;

    private final String name;

    public static final String TOPIC = "family_group";

    FamilyGroupTopicEnum(String tag, String name) {
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
