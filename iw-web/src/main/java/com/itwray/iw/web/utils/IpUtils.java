package com.itwray.iw.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * IP工具类
 *
 * @author wray
 * @since 2024/10/17
 */
public abstract class IpUtils {

    /**
     * 获取当前请求的客户端ip
     *
     * @return IP地址
     */
    public static String getCurrentClientIp() {
        return getClientIp(SpringWebHolder.getRequest());
    }

    /**
     * 获取客户端IP地址
     *
     * @param request 客户端请求
     * @return IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For 可能包含多个 IP 地址，第一个才是客户端的真实 IP
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isBlank(ip)) {
            return "0.0.0.0";
        }
        return ip;
    }
}
