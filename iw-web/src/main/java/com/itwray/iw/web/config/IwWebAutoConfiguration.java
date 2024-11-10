package com.itwray.iw.web.config;

import com.itwray.iw.web.client.AuthClient;
import com.itwray.iw.web.client.ClientHelper;
import com.itwray.iw.web.core.feign.FeignConfiguration;
import com.itwray.iw.web.core.mybatis.MybatisPlusConfiguration;
import com.itwray.iw.web.core.webmvc.IwWebMvcConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@Import({IwWebMvcConfiguration.class, MybatisPlusConfiguration.class, FeignConfiguration.class})
@ComponentScan(basePackages = "com.itwray.iw.web.service.impl")
public class IwWebAutoConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化AuthClient变量
        ClientHelper.setAuthClient(applicationContext.getBean(AuthClient.class));
    }
}
