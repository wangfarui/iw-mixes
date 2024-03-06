package com.itwray.iw.auth.core;

import com.itwray.iw.auth.model.ApiCode;
import com.itwray.iw.auth.model.GeneralApiCode;

/**
 * 授权服务异常
 *
 * @author wray
 * @since 2024/3/5
 */
public class AuthServiceException extends RuntimeException implements ApiCode {

    private int code = GeneralApiCode.SERVER_ERROR.getCode();

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

    public AuthServiceException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.code = apiCode.getCode();
    }

    public AuthServiceException(ApiCode apiCode, Throwable cause) {
        super(apiCode.getMessage(), cause);
        this.code = apiCode.getCode();
    }

    @Override
    public int getCode() {
        return code;
    }
}
