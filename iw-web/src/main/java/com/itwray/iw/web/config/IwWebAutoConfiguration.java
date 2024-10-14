package com.itwray.iw.web.config;

import com.itwray.iw.web.client.AuthClient;
import com.itwray.iw.web.client.ClientHelper;
import com.itwray.iw.web.core.ExceptionHandlerInterceptor;
import com.itwray.iw.web.core.GeneralResponseWrapperAdvice;
import com.itwray.iw.web.core.rocketmq.RocketMQConfiguration;
import com.itwray.iw.web.mybatis.MybatisPlusConfig;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * IW Web 自动装配配置类
 *
 * @author wray
 * @since 2024/4/3
 */
@AutoConfiguration
@EnableConfigurationProperties({IwWebProperties.class, IwDaoProperties.class})
@Import({IwWebConfig.class, MybatisPlusConfig.class, FeignConfiguration.class, RocketMQConfiguration.class})
@ComponentScan(basePackages = "com.itwray.iw.web.service.impl")
public class IwWebAutoConfiguration implements ApplicationContextAware {

    @Bean
    public ExceptionHandlerInterceptor exceptionHandlerInterceptor() {
        return new ExceptionHandlerInterceptor();
    }

    @Bean
    public GeneralResponseWrapperAdvice generalResponseWrapperAdvice() {
        return new GeneralResponseWrapperAdvice();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ClientHelper.setAuthClient(applicationContext.getBean(AuthClient.class));
    }
}
