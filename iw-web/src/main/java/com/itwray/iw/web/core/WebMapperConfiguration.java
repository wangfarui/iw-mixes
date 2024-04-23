package com.itwray.iw.web.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Web服务的 Mapper 配置类
 *
 * @author wray
 * @since 2024/3/2
 */
@Configuration(proxyBeanMethods = false)
@MapperScan(basePackages = "com.itwray.iw.*.mapper.*")
public class WebMapperConfiguration {
}
