package com.itwray.iw.web.core;

import com.itwray.iw.web.exception.IwWebException;
import com.itwray.iw.web.config.IwWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web服务配置器
 *
 * @author wray
 * @since 2024/3/5
 */
@Configuration(proxyBeanMethods = false)
public class WebServiceConfigurer implements WebMvcConfigurer {

    private IwWebProperties webProperties;

    @Autowired
    public void setWebProperties(IwWebProperties webProperties) {
        this.webProperties = webProperties;
    }



    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        IwWebProperties.Api api = this.getWebProperties().getApi();
        // 为所有带有 RestController 注解的controller添加前缀地址
        configurer.addPathPrefix(api.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class));
    }


    public IwWebProperties getWebProperties() {
        if (this.webProperties == null) {
            throw new IwWebException("webProperties 不能为空");
        }
        return this.webProperties;
    }
}
