package com.itwray.iw.starter.redis.config;

import com.itwray.iw.starter.redis.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis自动装配类
 *
 * @author wray
 * @since 2024/8/26
 */
@Configuration
@EnableConfigurationProperties(IwRedisProperties.class)
public class RedisAutoConfiguration {


    @Bean(name = "customRedisTemplate")
    @ConditionalOnMissingBean(name = "customRedisTemplate")
    public RedisTemplate<String, Object> customRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        RedisUtil.setRedisTemplate(template);
        return template;
    }
}
