package com.itwray.iw.gateway.config;

import com.itwray.iw.gateway.filter.DefaultGatewayFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Gateway服务的配置类
 *
 * @author wray
 * @since 2024/9/11
 */
@Configuration
@EnableConfigurationProperties(IwGatewayProperties.class)
public class IwGatewayConfiguration {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter defaultGatewayFilter(WebClient.Builder webClientBuilder, DiscoveryClient discoveryClient, IwGatewayProperties gatewayProperties) {
        return new DefaultGatewayFilter(webClientBuilder, discoveryClient, gatewayProperties);
    }

}
