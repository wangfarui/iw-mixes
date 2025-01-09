package com.itwray.iw.web.core.dingtalk;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉配置类
 *
 * @author wray
 * @since 2025/1/7
 */
@Configuration
@EnableConfigurationProperties(DingTalkProperties.class)
public class DingTalkConfiguration {

    /**
     * 钉钉配置前缀
     */
    public static final String DING_TALK_PREFIX = "iw.ding-talk";

    /**
     * 钉钉机器人客户端
     *
     * @param dingTalkProperties 钉钉配置
     */
    @ConditionalOnProperty(value = "iw.ding-talk.enabled", havingValue = "true")
    @Bean
    public DingTalkRobotClient dingTalkRobotClient(DingTalkProperties dingTalkProperties) {
        return new DingTalkRobotClient(dingTalkProperties);
    }

    /**
     * 钉钉配置监听器
     */
    @ConditionalOnBean(DingTalkRobotClient.class)
    @Bean
    public DingTalkConfigChangeListener dingTalkConfigChangeListener() {
        return new DingTalkConfigChangeListener();
    }
}
