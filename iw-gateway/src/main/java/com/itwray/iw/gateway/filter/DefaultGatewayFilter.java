package com.itwray.iw.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.common.ApiCode;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.constants.GeneralApiCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 默认网关全局过滤器
 *
 * @author wray
 * @since 2024/8/22
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultGatewayFilter implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;

    private final DiscoveryClient discoveryClient;

    @Autowired
    public DefaultGatewayFilter(WebClient.Builder webClientBuilder, DiscoveryClient discoveryClient) {
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 登录接口跳过token校验
        if (path != null) {
            if (path.startsWith("/auth-service/login/") || path.startsWith("/auth-service/authentication/validateToken")) {
                return chain.filter(exchange);
            }
        }
        String token = request.getHeaders().getFirst("iwtoken");
        if (token == null) {
            return createCustomResponse(exchange.getResponse(), GeneralApiCode.UNAUTHORIZED);
        }

        // 使用 DiscoveryClient 获取 iw-auth-service 的实例
        String authServiceUrl = discoveryClient.getInstances("iw-auth-service")
                .stream()
                .findAny()
                .map(instance -> instance.getUri().toString() + "/auth-service/authentication/validateToken?token=" + token)
                .orElse("http://localhost:18000/auth-service/authentication/validateToken?token=" + token);

        // 调用 auth-service 的 validateToken 接口
        return webClientBuilder.build()
                .get()
                .uri(authServiceUrl)  // 构建请求URI
                .retrieve()
                .bodyToMono(Map.class)  // 期望返回Boolean值
                .flatMap(resMap -> {
                    if (resMap != null && Boolean.TRUE.equals(resMap.get("data"))) {
                        // token 验证成功，继续执行链上的其他过滤器
                        return chain.filter(exchange);
                    } else {
                        // token 验证失败，返回未授权状态
                        return createCustomResponse(exchange.getResponse(), GeneralApiCode.UNAUTHORIZED);
                    }
                })
                .onErrorResume(e -> createCustomResponse(exchange.getResponse(), GeneralApiCode.UNAUTHORIZED));
    }

    private Mono<Void> createCustomResponse(ServerHttpResponse response, ApiCode apiCode) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        GeneralResponse<?> generalResponse = new GeneralResponse<>(apiCode.getCode(), apiCode.getMessage());

        return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONUtil.toJsonStr(generalResponse).getBytes())));
    }

}
