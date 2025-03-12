package com.itwray.iw.web.core.webmvc;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IwWebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public ExceptionHandlerInterceptor exceptionHandlerInterceptor() {
        return new ExceptionHandlerInterceptor();
    }

    @Bean
    public GeneralResponseWrapperAdvice generalResponseWrapperAdvice() {
        return new GeneralResponseWrapperAdvice();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并指定拦截的路径
        registry.addInterceptor(new WebHandlerInterceptor()).addPathPatterns("/**");  // 拦截所有路径
    }

    /**
     * web请求日志过滤器 配置类
     */
    @Configuration
    @Import({RequestLoggingFilter.class, ResponseLoggingFilter.class})
    public static class WebLoggingFilterConfiguration {

    }
}
