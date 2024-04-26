package com.itwray.iw.web.core;

import com.itwray.iw.common.GeneralApiCode;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.IwException;
import com.itwray.iw.web.exception.AuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理拦截器
 *
 * @author wray
 * @since 2024/3/5
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerInterceptor {

    @ExceptionHandler(AuthorizedException.class)
    public GeneralResponse<?> authExceptionHandler(AuthorizedException authorizedException) {
        return new GeneralResponse<>(GeneralApiCode.UNAUTHORIZED.getCode(), authorizedException.getMessage());
    }

    @ExceptionHandler(IwException.class)
    public GeneralResponse<?> iwExceptionHandler(IwException iwException) {
        log.error("[IW异常]" + iwException.getMessage(), iwException);
        return GeneralResponse.fail(iwException.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public GeneralResponse<?> defaultExceptionHandler(Throwable e) {
        log.error("系统异常", e);
        return GeneralResponse.fail();
    }
}
