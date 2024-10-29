package com.itwray.iw.web.core.mybatis;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * MyBatis 应用启动监听器
 *
 * @author wray
 * @since 2024/10/15
 */
public class MyBatisApplicationRunListener implements SpringApplicationRunListener {

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        environment.getSystemProperties().put("mybatis-plus.configuration.log-impl", Slf4jImpl.class);
        environment.getSystemProperties().put("mybatis-plus.type-handlers-package", "com.itwray.iw.web.mybatis.type.handlers");
    }
}
