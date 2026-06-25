package com.itwray.iw.external.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 静态博客访问控制配置。
 *
 * @author wray
 * @since 2026/6/24
 */
@Configuration
@EnableConfigurationProperties(BlogAccessProperties.class)
public class BlogAccessConfiguration {
}
