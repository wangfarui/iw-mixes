package com.itwray.iw.web.core.dingtalk;

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
     * 钉钉机器人客户端
     *
     * @param dingTalkProperties 钉钉配置
     * @return DingTalkClient
     */
    @Bean
    public DingTalkRobotClient dingTalkRobotClient(DingTalkProperties dingTalkProperties) {
        return new DingTalkRobotClient(dingTalkProperties);
    }
}
