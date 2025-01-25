package com.itwray.iw.web.core.dingtalk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.itwray.iw.web.client.DingTalkClient;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 钉钉客户端
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Slf4j
public class DingTalkRobotClient {

    private final DingTalkProperties properties;

    private static boolean CAN_APPLY;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    private static volatile DingTalkClient dingTalkClient;

    private static final Object DING_TALK_CLIENT_LOCK = new Object();

    @SuppressWarnings("all")
    public DingTalkRobotClient(DingTalkProperties properties) {
        this.properties = properties;
        changeCanApply(Optional.ofNullable(properties.getEnabled()).orElse(false));
    }

    /**
     * 发送钉钉消息
     *
     * @param request 机器人消息对象
     */
    public void send(final DingTalkSendRequest request) {
        send(request, CAN_APPLY);
    }

    /**
     * 发送钉钉消息
     *
     * @param request 机器人消息对象
     */
    public void send(final DingTalkSendRequest request, boolean canApply) {
        if (canApply) {
            if (Boolean.TRUE.equals(properties.getAt().getAtAll())) {
                request.setAtAll(properties.getAt().getAtAll());
            } else if (CollUtil.isNotEmpty(properties.getAt().getAtMobiles())) {
                request.setAtMobiles(properties.getAt().getAtMobiles());
            }

            boolean completed = request.completeRequestParam();
            if (!completed) {
                log.warn("[DingTalkClient][send]钉钉消息请求对象数据异常, request:{}", JSONUtil.toJsonStr(request));
            }
            request.setRequestUrl(properties.getRequestUrl());
            EXECUTOR_SERVICE.execute(() -> {
                getDingTalkClient().sendDingTalkRobotMsg(request);
            });
        }
    }

    public static void changeCanApply(boolean canApply) {
        CAN_APPLY = canApply;
    }

    private static DingTalkClient getDingTalkClient() {
        if (dingTalkClient == null) {
            synchronized (DING_TALK_CLIENT_LOCK) {
                if (dingTalkClient == null) {
                    dingTalkClient = ApplicationContextHolder.getBean(DingTalkClient.class);
                }
            }
        }

        return dingTalkClient;
    }
}
