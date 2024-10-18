package com.itwray.iw.gateway.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.annotation.Transient;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Gateway服务属性配置
 *
 * @author wray
 * @since 2024/9/11
 */
@Data
@ConfigurationProperties(prefix = "iw.gateway")
@Validated
public class IwGatewayProperties {

    @NotNull(message = "授权服务名不能为空")
    private String authServiceName;

    /**
     * 授权校验url地址
     */
    @NotNull(message = "授权校验url地址不能为空")
    private String authValidateUrl;

    /**
     * 忽略校验的api接口
     */
    @NotEmpty(message = "忽略校验的api接口不能为空")
    private Set<String> ignoreValidateApi;

    /**
     * 忽略校验的服务名
     */
    @NotEmpty(message = "忽略校验的服务名不能为空")
    private Set<String> ignoreValidateService;

    /**
     * token有效时间(默认3s)
     * <p>单位：s(秒)</p>
     */
    private Long tokenValidTime = 3L;

    /**
     * 忽略校验的请求地址缓存
     */
    @Transient
    private final Cache<String, Boolean> ignoreValidatePathCache;

    public IwGatewayProperties() {
        // 初始化缓存对象
        ignoreValidatePathCache = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES) // 10分钟内没有访问就失效
                .maximumSize(100) // 最大缓存条目数为100
                .build();
    }

    /**
     * 请求路径是否为可以忽略校验的接口
     *
     * @param path 请求路径
     * @return true -> 忽略校验
     */
    public boolean isIgnoreValidateApi(String path) {
        return ignoreValidatePathCache.get(path, p -> {
            int index = p.indexOf("/", 1);
            if (index > 1) {
                String serviceName = p.substring(1, index);
                if (ignoreValidateService.contains(serviceName)) {
                    return true;
                }
            }
            return ignoreValidateApi.contains(p);
        });
    }
}
