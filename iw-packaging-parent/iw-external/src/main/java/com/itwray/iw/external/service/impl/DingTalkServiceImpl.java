package com.itwray.iw.external.service.impl;

import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.external.service.DingTalkService;
import com.itwray.iw.web.core.dingtalk.DingTalkSendRequest;
import com.itwray.iw.web.core.dingtalk.DingTalkSendResponse;
import com.itwray.iw.web.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钉钉服务实现层
 *
 * @author wray
 * @since 2025/1/25
 */
@Service
@Slf4j
public class DingTalkServiceImpl implements DingTalkService {

    @Override
    public void sendDingTalkRobotMsg(DingTalkSendRequest request) {
        DingTalkSendResponse response = HttpUtils.createRequest(Method.POST, request.getRequestUrl())
                .setBody(request)
                .executePost(DingTalkSendResponse.class);
        if (response == null) {
            log.warn("[DingTalkClient][send]发送钉钉消息异常, request:{}", JSONUtil.toJsonStr(request));
        } else if (response.getErrcode() != 0) {
            log.warn("[DingTalkClient][send]发送钉钉消息失败, request:{}, response:{}", JSONUtil.toJsonStr(request), JSONUtil.toJsonStr(response));
        }
    }
}
