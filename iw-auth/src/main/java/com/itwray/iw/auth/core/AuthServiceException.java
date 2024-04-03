package com.itwray.iw.auth.core;

import com.itwray.iw.common.IwException;

/**
 * 认证服务异常
 *
 * @author wray
 * @since 2024/3/5
 */
public class AuthServiceException extends IwException {

    public AuthServiceException(String message) {
        super(message);
    }
}
