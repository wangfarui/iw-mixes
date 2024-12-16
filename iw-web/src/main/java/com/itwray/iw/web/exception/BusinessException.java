package com.itwray.iw.web.exception;

/**
 * 项目业务异常
 * <p>该异常message信息是会返回给客户端的, power by {@link com.itwray.iw.web.core.webmvc.ExceptionHandlerInterceptor#businessExceptionHandler(BusinessException)}</p>
 *
 * @author wray
 * @since 2024/12/16
 */
public class BusinessException extends IwWebException {

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}
