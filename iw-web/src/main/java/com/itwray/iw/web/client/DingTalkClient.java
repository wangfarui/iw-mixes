package com.itwray.iw.web.client;

import com.itwray.iw.web.core.dingtalk.DingTalkSendRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SMS短信 Client
 *
 * @author wray
 * @since 2024/12/19
 */
@FeignClient(value = "iw-external-service", path = "/external-service/internal/dingTalk")
public interface DingTalkClient {

    @PostMapping("/sendDingTalkRobotMsg")
    @Operation(summary = "发送钉钉机器人消息")
    void sendDingTalkRobotMsg(@RequestBody DingTalkSendRequest request);
}

