package com.itwray.iw.auth.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 认证服务的 Mapper 配置类
 *
 * @author wray
 * @since 2024/3/2
 */
@Configuration(proxyBeanMethods = false)
@MapperScan
public class AuthMapperConfiguration {
}
