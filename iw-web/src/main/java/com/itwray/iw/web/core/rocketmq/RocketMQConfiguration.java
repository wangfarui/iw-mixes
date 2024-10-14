package com.itwray.iw.web.core.rocketmq;

import com.itwray.iw.web.utils.MQProducerHelper;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import static org.apache.rocketmq.client.autoconfigure.RocketMQAutoConfiguration.ROCKETMQ_TEMPLATE_DEFAULT_GLOBAL_NAME;

/**
 * RocketMQ配置类
 *
 * @author wray
 * @since 2024/10/14
 */
@Configuration
public class RocketMQConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext.containsBean(ROCKETMQ_TEMPLATE_DEFAULT_GLOBAL_NAME)) {
            RocketMQClientTemplate rocketMQClientTemplate = applicationContext.getBean(
                    ROCKETMQ_TEMPLATE_DEFAULT_GLOBAL_NAME, RocketMQClientTemplate.class
            );
            // 默认生产者不为空时，才赋予MQProducerHelper生产功能
            if (rocketMQClientTemplate.getProducerBuilder() != null) {
                MQProducerHelper.setRocketMQClientTemplate(rocketMQClientTemplate);
            }
        }
    }
}
