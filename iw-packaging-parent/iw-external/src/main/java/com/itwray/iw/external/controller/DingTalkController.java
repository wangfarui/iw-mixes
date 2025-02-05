package com.itwray.iw.external.controller;

import com.itwray.iw.external.model.ExternalClientConstants;
import com.itwray.iw.external.service.DingTalkService;
import com.itwray.iw.web.core.dingtalk.DingTalkSendRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉接口控制层
 *
 * @author wray
 * @since 2025/1/25
 */
@RestController
@RequestMapping(ExternalClientConstants.INTERNAL_PATH_PREFIX + "/dingTalk")
@Tag(name = "SMS短信服务接口（内部服务使用）")
public class DingTalkController {

    private final DingTalkService dingTalkService;

    public DingTalkController(DingTalkService dingTalkService) {
        this.dingTalkService = dingTalkService;
    }

    @PostMapping("/sendDingTalkRobotMsg")
    public void sendDingTalkRobotMsg(@RequestBody DingTalkSendRequest request) {
        dingTalkService.sendDingTalkRobotMsg(request);
    }
}
