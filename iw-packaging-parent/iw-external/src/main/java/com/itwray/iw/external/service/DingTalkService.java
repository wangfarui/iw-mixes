package com.itwray.iw.external.service;

import com.itwray.iw.web.core.dingtalk.DingTalkSendRequest;

/**
 * 钉钉服务接口
 *
 * @author wray
 * @since 2025/1/25
 */
public interface DingTalkService {

    void sendDingTalkRobotMsg(DingTalkSendRequest request);
}
