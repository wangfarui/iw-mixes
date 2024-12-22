package com.itwray.iw.web.config;

import com.itwray.iw.web.core.EnvironmentHolder;
import com.itwray.iw.web.core.feign.FeignConfiguration;
import com.itwray.iw.web.core.mybatis.MybatisPlusConfiguration;
import com.itwray.iw.web.core.webmvc.IwWebMvcConfiguration;
import com.itwray.iw.web.utils.ApplicationContextHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@Import({IwWebMvcConfiguration.class, MybatisPlusConfiguration.class, FeignConfiguration.class})
@ComponentScan(basePackages = "com.itwray.iw.web.service.impl")
public class IwWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHolder.class)
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean(EnvironmentHolder.class)
    public EnvironmentHolder environmentHolder() {
        return new EnvironmentHolder();
    }
}
