package com.itwray.iw.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * 动态内部微服务路由配置
 */
@Configuration
public class DynamicInternalRouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("internal-dynamic-router", r -> r
                        .path("/internal/{service}/**") // 匹配所有 /internal/xxx/** 的请求
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    // 提取 service 服务名称
                                    String serviceName = exchange.getRequest()
                                            .getPath()
                                            .toString()
                                            .split("/")[2];

                                    // 构造新的URI
                                    String lbUri = "lb://iw-" + serviceName;
                                    URI uri = URI.create(lbUri);
                                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, uri);
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("no://op") // 需要一个占位 URI，实际不会用它
                )
                .build();
    }
}
