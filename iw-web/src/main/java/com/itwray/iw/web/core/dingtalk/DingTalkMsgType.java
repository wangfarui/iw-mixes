package com.itwray.iw.web.core.dingtalk;

/**
 * 钉钉消息类型
 *
 * @author wangfarui
 * @since 2022/10/18
 */
public enum DingTalkMsgType {
    TEXT("text"),
    LINK("link"),
    MARKDOWN("markdown"),
    ACTION_CARD("actionCard"),
    FEED_CARD("feedCard");

    private final String type;

    DingTalkMsgType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
