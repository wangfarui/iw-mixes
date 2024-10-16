package com.itwray.iw.web.mybatis;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * MyBatis应用启动监听器
 *
 * @author wray
 * @since 2024/10/15
 */
public class MyBatisApplicationRunListener implements SpringApplicationRunListener {

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        environment.getSystemProperties().put("mybatis-plus.configuration.log-impl", StdOutImpl.class);
        environment.getSystemProperties().put("mybatis-plus.type-handlers-package", "com.itwray.iw.web.mybatis.type.handlers");
    }
}
