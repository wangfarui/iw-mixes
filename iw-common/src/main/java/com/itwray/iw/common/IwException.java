package com.itwray.iw.common;

import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.common.utils.ExceptionUtils;

/**
 * IW项目顶层异常对象
 * <p>项目内所有自定义异常对象，都应该继承该对象。
 * <p>经由{@link ExceptionUtils#isInternalException(RuntimeException)}方法验证时，必定返回true。
 * <p>可以通过{@link ExceptionUtils#extractIwException(RuntimeException)}方法提取IwException对象。
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

    public IwException(Integer code, String message) {
        super(message);
        this.code = code;
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
