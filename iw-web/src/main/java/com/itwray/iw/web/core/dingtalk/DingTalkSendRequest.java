package com.itwray.iw.web.core.dingtalk;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 钉钉发送消息请求对象
 * <p>对象参数名与钉钉开发平台提供的入参名保持一致</p>
 *
 * @author wangfarui
 * @since 2025/1/6
 */
public class DingTalkSendRequest {

    /**
     * 用于钉钉入参
     */
    @Getter
    private String msgtype;

    /**
     * 钉钉消息类型
     */
    @Setter
    private DingTalkMsgType dingTalkMsgType;

    /**
     * 钉钉消息需要 @ 的对象
     */
    @Setter
    @Getter
    private AT at;

    /**
     * 钉钉消息类型为 markdown 的内容
     */
    @Setter
    @Getter
    private Markdown markdown;

    /**
     * 钉钉消息类型为 text 的内容
     */
    @Setter
    @Getter
    private Text text;

    /**
     * 完善请求对象参数（在发送请求之前）
     */
    public boolean completeRequestParam() {
        DingTalkMsgType msgType = determineMsgType();
        if (msgType == null) return false;
        this.msgtype = msgType.getType();
        if (DingTalkMsgType.MARKDOWN.equals(msgType)) {
            this.getMarkdown().formatContentToText();
            if (CollUtil.isNotEmpty(this.getAt().getAtMobiles())) {
                StringBuilder atMobiles = new StringBuilder("\n\n");
                for (String mobile : this.getAt().getAtMobiles()) {
                    atMobiles.append("@").append(mobile).append(" ");
                }
                this.markdown.text += atMobiles.toString();
            }
        }
        return true;
    }

    public void setAtAll(boolean atAll) {
        validAt();
        this.at.setAtAll(atAll);
    }

    public void setAtMobiles(List<String> atMobiles) {
        validAt();
        this.at.setAtMobiles(atMobiles);
    }

    public void setAtUserIds(List<String> atUserIds) {
        validAt();
        this.at.setAtUserIds(atUserIds);
    }

    public void setMarkdownTitle(String title) {
        validMarkdown();
        this.markdown.setTitle(title);
    }

    public void addMarkdownContent(String key, String value) {
        validMarkdown();
        this.markdown.addContent(key, value);
    }

    public void setTextContent(String content) {
        if (this.text == null) {
            this.text = new Text();
        }
        this.text.setContent(content);
    }

    private void validAt() {
        if (this.at == null) {
            this.at = new AT();
        }
    }

    private void validMarkdown() {
        if (this.markdown == null) {
            this.markdown = new Markdown();
        }
    }

    private DingTalkMsgType determineMsgType() {
        if (this.markdown != null) {
            this.dingTalkMsgType = DingTalkMsgType.MARKDOWN;
        } else if (this.text != null) {
            this.dingTalkMsgType = DingTalkMsgType.TEXT;
        }
        return this.dingTalkMsgType;
    }

    @Setter
    @Getter
    public static class AT {
        private List<String> atMobiles;

        private List<String> atUserIds;

        private boolean isAtAll;
    }

    public static class Markdown {
        @Setter
        @Getter
        private String title;

        @Setter
        @Getter
        private String text;

        /**
         * 非钉钉消息的数据格式
         */
        private Map<String, String> content;

        public void addContent(String key, String value) {
            if (this.content == null) {
                this.content = new LinkedHashMap<>();
            }
            this.content.put(key, value);
        }

        public void formatContentToText() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : this.content.entrySet()) {
                sb.append(entry.getKey()).append("：").append(entry.getValue()).append("\n\n");
            }
            this.text = sb.toString();
        }
    }

    @Setter
    @Getter
    public static class Text {
        private String content;
    }

}
