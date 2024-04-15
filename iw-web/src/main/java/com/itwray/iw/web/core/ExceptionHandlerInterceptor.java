package com.itwray.iw.web.core;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.IwException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理拦截器
 *
 * @author wray
 * @since 2024/3/5
 */
@RestControllerAdvice
public class ExceptionHandlerInterceptor {

    @ExceptionHandler(IwException.class)
    public GeneralResponse<?> iwExceptionHandler(IwException iwException) {
        return GeneralResponse.fail(iwException.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public GeneralResponse<?> defaultExceptionHandler() {
        return GeneralResponse.fail();
    }
}
