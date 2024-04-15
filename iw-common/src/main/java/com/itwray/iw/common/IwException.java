package com.itwray.iw.common;

/**
 * IW Project Exception
 *
 * @author wray
 * @since 2024/4/3
 */
public class IwException extends RuntimeException implements ApiCode {

    private int code = GeneralApiCode.INTERNAL_SERVER_ERROR.getCode();

    public IwException() {
    }

    public IwException(String message) {
        super(message);
    }

    public IwException(String message, Throwable cause) {
        super(message, cause);
    }

    public IwException(Throwable cause) {
        super(cause);
    }

    public IwException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.code = apiCode.getCode();
    }

    public IwException(ApiCode apiCode, Throwable cause) {
        super(apiCode.getMessage(), cause);
        this.code = apiCode.getCode();
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
