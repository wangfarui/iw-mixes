package com.itwray.iw.gateway.filter;

import com.itwray.iw.gateway.config.IwGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * IP检查过滤器
 *
 * @author wray
 * @since 2024/12/20
 */
@Component
public class IpCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<IpCheckGatewayFilterFactory.IpCheckConfig> implements Ordered {

    private IwGatewayProperties gatewayProperties;

    public IpCheckGatewayFilterFactory() {
        super(IpCheckConfig.class);
    }

    @Autowired
    public void setGatewayProperties(IwGatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public GatewayFilter apply(IpCheckConfig config) {
        return ((exchange, chain) -> {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress == null) {
                // 无法判断的请求ip，拒绝请求
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            // 获取客户端 IP
            String clientIp = remoteAddress.getAddress().getHostAddress();

            // 校验 IP 是否在允许的内网 IP 列表中
            if (gatewayProperties == null || !gatewayProperties.getAllowedInternalIps().contains(clientIp)) {
                // 如果不是内网 IP，拒绝请求
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // 如果是内网 IP，继续处理请求
            return chain.filter(exchange);
        });
    }

    @Override
    public int getOrder() {
        // 确保过滤器优先级最高
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public static class IpCheckConfig {

    }
}
