package com.itwray.iw.web.core.tracing;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 链路跟踪 应用启动监听器
 *
 * @author wray
 * @since 2024/10/28
 */
public class TracingApplicationRunListener implements SpringApplicationRunListener {

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        // 1.0采样率 表示采样全部日志
        environment.getSystemProperties().put("management.tracing.sampling.probability", 1.0);
    }
}
