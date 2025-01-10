package com.itwray.iw.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.starter.redis.CommonRedisKeyEnum;
import com.itwray.iw.starter.redis.RedisUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 密钥检查过滤器
 *
 * @author wray
 * @since 2024/12/20
 */
@Component
public class SecretCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<SecretCheckGatewayFilterFactory.SecretCheckConfig> implements Ordered {

    public SecretCheckGatewayFilterFactory() {
        super(SecretCheckConfig.class);
    }

    @Override
    public GatewayFilter apply(SecretCheckConfig config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            List<String> secrets = request.getHeaders().get(RequestHeaderConstants.SECRET_HEADER_KEY);
            if (CollUtil.isEmpty(secrets)) {
                // 密钥为空，拒绝请求
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            String redisKey = CommonRedisKeyEnum.FEIGN_SECRET_KEY.getKey(secrets.get(0));
            if (!RedisUtil.hasKey(redisKey)) {
                // 密钥失效，拒绝请求
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            // 移除密钥缓存, 确保密钥只可被使用一次
            RedisUtil.delete(redisKey);
            return chain.filter(exchange);
        });
    }

    @Override
    public int getOrder() {
        // 确保过滤器优先级最高
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    public static class SecretCheckConfig {

    }
}
