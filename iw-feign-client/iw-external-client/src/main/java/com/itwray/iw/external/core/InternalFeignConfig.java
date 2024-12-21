package com.itwray.iw.external.core;

import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.starter.redis.RedisKeyConstants;
import com.itwray.iw.starter.redis.RedisUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * 内部Feign配置类
 *
 * @author wray
 * @since 2024/12/19
 */
@Configuration
public class InternalFeignConfig {

    @Bean("internalRequestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String uuid = UUID.randomUUID().toString();
            // 60s有效期
            RedisUtil.set(RedisKeyConstants.FEIGN_SECRET_KEY + uuid, uuid, 60);
            requestTemplate.header(RequestHeaderConstants.SECRET_HEADER_KEY, uuid);
        };
    }
}
