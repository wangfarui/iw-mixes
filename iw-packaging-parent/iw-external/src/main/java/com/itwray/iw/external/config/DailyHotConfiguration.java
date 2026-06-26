package com.itwray.iw.external.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 每日热点配置。
 *
 * @author wray
 * @since 2026/6/26
 */
@Configuration
@EnableConfigurationProperties(DailyHotProperties.class)
public class DailyHotConfiguration {
}
