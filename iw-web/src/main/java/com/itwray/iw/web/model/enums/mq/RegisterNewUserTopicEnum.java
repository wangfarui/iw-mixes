package com.itwray.iw.web.model.enums.mq;

import com.itwray.iw.web.constants.MQTopicConstants;
import lombok.Getter;

/**
 * 注册新用户 枚举
 *
 * @author wray
 * @since 2025/4/8
 */
@Getter
public enum RegisterNewUserTopicEnum implements MQDestination {

    INIT("init", "初始化"),
    DEPEND_DICT("dependDict", "依赖字典数据"),
    ;

    private final String tag;

    private final String name;

    RegisterNewUserTopicEnum(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    @Override
    public String getTopic() {
        return MQTopicConstants.REGISTER_NEW_USER;
    }

    @Override
    public String getDestination() {
        return MQTopicConstants.REGISTER_NEW_USER + ":" + getTag();
    }
}
