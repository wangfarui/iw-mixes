package com.itwray.iw.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.common.constants.RequestHeaderConstants;
import com.itwray.iw.gateway.config.IwGatewayProperties;
import com.itwray.iw.starter.redis.RedisUtil;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
public class DefaultGatewayFilter implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;

    private final DiscoveryClient discoveryClient;

    private final IwGatewayProperties gatewayProperties;

    private String authServiceUrlCache;

    public DefaultGatewayFilter(WebClient.Builder webClientBuilder, DiscoveryClient discoveryClient, IwGatewayProperties gatewayProperties) {
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 登录接口跳过token校验
        if (path != null && gatewayProperties.isIgnoreValidateApi(path)) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst(RequestHeaderConstants.TOKEN_HEADER);
        if (token == null) {
            return createUnauthorizedResponse(exchange.getResponse(), "未登录，请登录后再试");
        }

        Boolean status = (Boolean) RedisUtil.get(token);
        // 不为空，表示近期token的有效结果已被验证过，短时间内无须校验
        if (status != null) {
            if (status) {
                return chain.filter(exchange);
            } else {
                return createUnauthorizedResponse(exchange.getResponse(), "登录状态已失效，请重新登录");
            }
        }

        // 调用 auth-service 的 validateToken 接口
        return webClientBuilder.build()
                .get()
                .uri(getAuthServiceUrl() + token)  // 构建请求URI
                .retrieve()
                .bodyToMono(Boolean.class)  // 期望返回Boolean值
                .flatMap(res -> {
                    if (Boolean.TRUE.equals(res)) {
                        RedisUtil.set(token, Boolean.TRUE, gatewayProperties.getTokenValidTime());
                        // token 验证成功，继续执行链上的其他过滤器
                        return chain.filter(exchange);
                    } else {
                        // token 验证失败，返回未授权状态
                        RedisUtil.set(token, Boolean.FALSE, gatewayProperties.getTokenValidTime());
                        return createUnauthorizedResponse(exchange.getResponse(), "登录状态已失效，请重新登录");
                    }
                })
                .onErrorResume(e -> createUnauthorizedResponse(exchange.getResponse(), "未授权"));
    }

    private Mono<Void> createUnauthorizedResponse(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        GeneralResponse<?> generalResponse = new GeneralResponse<>(GeneralApiCode.UNAUTHORIZED.getCode(), message);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONUtil.toJsonStr(generalResponse).getBytes())));
    }

    private String getAuthServiceUrl() {
        if (authServiceUrlCache == null) {
            // 使用 DiscoveryClient 获取 iw-auth-service 的实例
            authServiceUrlCache = discoveryClient.getInstances(gatewayProperties.getAuthServiceName())
                    .stream()
                    .findAny()
                    .map(instance -> instance.getUri().toString())
                    .orElse("http://localhost:18000")
                    + gatewayProperties.getAuthValidateUrl() + "?token=";
        }
        return authServiceUrlCache;
    }
}
