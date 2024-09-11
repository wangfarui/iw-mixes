package com.itwray.iw.gateway.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

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
     * token有效时间(默认3s)
     * <p>单位：s(秒)</p>
     */
    private Long tokenValidTime = 3L;

    /**
     * 请求路径是否为可以忽略校验的接口
     *
     * @param path 请求路径
     * @return true -> 忽略校验
     */
    public boolean isIgnoreValidateApi(String path) {
        return ignoreValidateApi.contains(path);
    }
}
