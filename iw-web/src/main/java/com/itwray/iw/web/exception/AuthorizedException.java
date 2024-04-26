package com.itwray.iw.web.exception;

/**
 * 授权异常
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
