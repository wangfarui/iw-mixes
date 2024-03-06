package com.itwray.iw.auth.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 授权Web服务配置器
 *
 * @author wray
 * @since 2024/3/5
 */
@Configuration
public class AuthWebServiceConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ExceptionHandlerInterceptor());
    }
}
