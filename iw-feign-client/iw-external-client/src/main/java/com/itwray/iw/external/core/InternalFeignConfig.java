package com.itwray.iw.external.core;

import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.starter.redis.CommonRedisKeyEnum;
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
            CommonRedisKeyEnum.FEIGN_SECRET_KEY.setStringValue(uuid, uuid);
            requestTemplate.header(RequestHeaderConstants.SECRET_HEADER_KEY, uuid);
        };
    }
}
