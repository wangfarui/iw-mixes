package com.itwray.iw.auth.core;

/**
 * 授权服务异常
 *
 * @author wangfarui
 * @since 2024/3/5
 */
public class AuthServiceException extends RuntimeException {

    public AuthServiceException() {
    }

    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthServiceException(Throwable cause) {
        super(cause);
    }

    public AuthServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
