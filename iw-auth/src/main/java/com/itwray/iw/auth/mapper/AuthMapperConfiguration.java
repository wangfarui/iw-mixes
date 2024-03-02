package com.itwray.iw.auth.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 授权服务的 Mapper 配置类
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@Configuration(proxyBeanMethods = false)
@MapperScan
public class AuthMapperConfiguration {
}
