package com.itwray.iw.common.constants;

/**
 * 请求头常量
 *
 * @author wray
 * @since 2024/12/20
 */
public abstract class RequestHeaderConstants {

    /**
     * token的固定header
     */
    public static final String TOKEN_HEADER = "iwtoken";

    /**
     * 服务内部调用的密钥头
     */
    public static final String SECRET_HEADER_KEY = "Iw-Feign-Secret";
}
