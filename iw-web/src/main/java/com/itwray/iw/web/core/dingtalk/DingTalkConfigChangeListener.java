package com.itwray.iw.web.core.dingtalk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;

/**
 * 钉钉配置信息变更监听器
 *
 * @author wangfarui
 * @since 2022/10/18
 */
@Slf4j
public class DingTalkConfigChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        for (String key : event.getKeys()) {
            if (key.startsWith(DingTalkConfiguration.DING_TALK_PREFIX)) {
                DingTalkProperties.clearCache();
                break;
            }
        }
    }
}
