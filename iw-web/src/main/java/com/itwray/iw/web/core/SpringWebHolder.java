package com.itwray.iw.web.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Spring Web Holder
 *
 * @author wray
 * @since 2024/8/22
 */
public class SpringWebHolder {

    private SpringWebHolder() {
    }

    /**
     * 获取当前线程的Http请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new IllegalStateException("非 web 上下文无法获取 HttpServletRequest");
        } else {
            return servletRequestAttributes.getRequest();
        }
    }

    /**
     * 获取当前线程的Http响应对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new IllegalStateException("非 web 上下文无法获取 HttpServletRequest");
        } else {
            return servletRequestAttributes.getResponse();
        }
    }

    /**
     * 获取请求头信息
     *
     * @param headerName header名称
     * @return header值
     */
    public static String getHeader(String headerName) {
        HttpServletRequest request = getRequest();
        return request.getHeader(headerName);
    }

    /**
     * 判断当前线程是否来自于HTTP Web请求
     *
     * @return true -> 是来自于HTTP Web请求
     */
    public static boolean isWeb() {
        return RequestContextHolder.getRequestAttributes() != null;
    }
}