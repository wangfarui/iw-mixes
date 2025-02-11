package com.itwray.iw.starter.rocketmq.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.itwray.iw.starter.rocketmq.MQProducerHelper;
import com.itwray.iw.starter.rocketmq.web.RocketMQDataDaoHolder;
import com.itwray.iw.starter.rocketmq.web.dao.BaseMqConsumeRecordsDao;
import com.itwray.iw.starter.rocketmq.web.dao.BaseMqProduceRecordsDao;
import com.itwray.iw.starter.rocketmq.web.mapper.BaseMqConsumeRecordsMapper;
import jakarta.annotation.PostConstruct;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.apache.rocketmq.client.autoconfigure.RocketMQAutoConfiguration.ROCKETMQ_TEMPLATE_DEFAULT_GLOBAL_NAME;

/**
 * RocketMQ配置类
 *
 * @author wray
 * @since 2024/10/14
 */
@Configuration
public class RocketMQAutoConfiguration implements ApplicationContextAware {

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

    @Configuration
    @ConditionalOnBean(MybatisPlusAutoConfiguration.class)
    @MapperScan(basePackageClasses = BaseMqConsumeRecordsMapper.class)
    @Import({BaseMqConsumeRecordsDao.class, BaseMqProduceRecordsDao.class})
    public static class RocketMQDataDaoConfiguration {

        private final ApplicationContext applicationContext;

        @Autowired
        public RocketMQDataDaoConfiguration(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @PostConstruct
        public void init() {
            BaseMqConsumeRecordsDao baseMqConsumeRecordsDao = applicationContext.getBean(BaseMqConsumeRecordsDao.class);
            RocketMQDataDaoHolder.setBaseMqConsumeRecordsDao(baseMqConsumeRecordsDao);

            BaseMqProduceRecordsDao baseMqProduceRecordsDao = applicationContext.getBean(BaseMqProduceRecordsDao.class);
            RocketMQDataDaoHolder.setBaseMqProduceRecordsDao(baseMqProduceRecordsDao);

            String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
            RocketMQDataDaoHolder.setApplicationName(applicationName);
        }
    }
}
