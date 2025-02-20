package com.itwray.iw.external.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * 外部服务的客户端常量
 *
 * @author wray
 * @since 2024/12/19
 */
public abstract class ExternalClientConstants {

    /**
     * 外部服务名
     */
    @Value("${spring.application.name}")
    public static final String SERVICE_NAME = "iw-external-service";

    /**
     * 所有服务实例名称
     */
    public static final String[] ALL_SERVICE_NAME = {SERVICE_NAME, "iw-gateway-service", "iw-auth-service", "iw-bookkeeping-service"};

    /**
     * 外部服务固定前缀
     */
    public static final String SERVICE_PATH_PREFIX = "/external-service";

    /**
     * 内部地址前缀
     */
    public static final String INTERNAL_PATH_PREFIX = "/internal";

    /**
     * external-service的内部服务地址
     */
    public static final String INTERNAL_SERVICE_PATH = SERVICE_PATH_PREFIX + INTERNAL_PATH_PREFIX;

}
