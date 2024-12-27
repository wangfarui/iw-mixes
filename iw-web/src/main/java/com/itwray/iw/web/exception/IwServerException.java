package com.itwray.iw.web.exception;

import com.itwray.iw.common.IwException;

/**
 * IW 服务器异常
 * <p>该类异常直接返回http code为500, 它应该是不能被客户端正常处理的异常, 但可以看到异常信息</p>
 *
 * @author wray
 * @since 2024/12/27
 */
public class IwServerException extends IwException {

    public IwServerException() {
    }

    public IwServerException(String message) {
        super(message);
    }

    public IwServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IwServerException(Throwable cause) {
        super(cause);
    }

    public IwServerException(Integer code, String message) {
        super(code, message);
    }
}
