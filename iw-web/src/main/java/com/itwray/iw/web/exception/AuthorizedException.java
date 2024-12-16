package com.itwray.iw.web.exception;

/**
 * 授权异常
 * <p>该异常会返回401, 引导客户端重定向到登录页面, power by {@link com.itwray.iw.web.core.webmvc.ExceptionHandlerInterceptor#authExceptionHandler(AuthorizedException)}</p>
 *
 * @author wray
 * @since 2024/4/26
 */
public class AuthorizedException extends IwWebException {

    public AuthorizedException() {
    }

    public AuthorizedException(String message) {
        super(message);
    }
}
