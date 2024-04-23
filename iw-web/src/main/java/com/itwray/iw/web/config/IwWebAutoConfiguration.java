package com.itwray.iw.web.config;

import com.itwray.iw.web.core.ExceptionHandlerInterceptor;
import com.itwray.iw.web.core.GeneralResponseWrapperAdvice;
import com.itwray.iw.web.core.WebMapperConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * IW Web 自动装配配置类
 *
 * @author wray
 * @since 2024/4/3
 */
@AutoConfiguration
//@EnableConfigurationProperties(IwWebProperties.class)
@Import(WebMapperConfiguration.class)
public class IwWebAutoConfiguration {

    @Bean
    public ExceptionHandlerInterceptor exceptionHandlerInterceptor() {
        return new ExceptionHandlerInterceptor();
    }

    @Bean
    public GeneralResponseWrapperAdvice generalResponseWrapperAdvice() {
        return new GeneralResponseWrapperAdvice();
    }
}
