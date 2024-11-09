package com.itwray.iw.starter.redis.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;

/**
 * Redis 属性初始化器
 *
 * @author wray
 * @since 2024/9/6
 */
public class IwRedisPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Binder binder = Binder.get(environment);
        BindResult<IwRedisProperties> bindResult = binder.bind("iw.redis", IwRedisProperties.class);
        if (!bindResult.isBound()) {
            return;
        }
        IwRedisProperties iwRedisProperties = bindResult.get();

        HashMap<String, Object> redisProperties = new HashMap<>();
        redisProperties.put("spring.data.redis.host", iwRedisProperties.getHost());
        redisProperties.put("spring.data.redis.port", iwRedisProperties.getPort());
        redisProperties.put("spring.data.redis.database", iwRedisProperties.getDatabase());
        redisProperties.put("spring.data.redis.username", iwRedisProperties.getUsername());
        redisProperties.put("spring.data.redis.password", iwRedisProperties.getPassword());
        redisProperties.put("spring.data.redis.client-type", RedisProperties.ClientType.LETTUCE);
        redisProperties.put("spring.data.redis.lettuce.pool.enabled", Boolean.TRUE);
        MapPropertySource propertySource = new MapPropertySource("redisCustomPropertySource", redisProperties);

        environment.getPropertySources().addLast(propertySource);
    }
}
