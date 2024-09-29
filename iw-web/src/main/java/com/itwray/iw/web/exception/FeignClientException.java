package com.itwray.iw.web.exception;

/**
 * Feign Client调用异常
 *
 * @author wray
 * @since 2024/9/29
 */
public class FeignClientException extends IwWebException {

    public FeignClientException() {
    }

    public FeignClientException(String message) {
        super(message);
    }

    public FeignClientException(Integer code, String message) {
        super(code, message);
    }
}
