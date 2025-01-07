package com.itwray.iw.web.core.dingtalk;

import lombok.Getter;
import lombok.Setter;

/**
 * 钉钉发送消息响应对象
 *
 * @author wangfarui
 * @since 2025/1/6
 */
@Setter
@Getter
public class DingTalkSendResponse {

    private long errcode;

    private String errmsg;
}
