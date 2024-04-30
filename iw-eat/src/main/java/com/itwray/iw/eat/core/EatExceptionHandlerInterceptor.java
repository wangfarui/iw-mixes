package com.itwray.iw.eat.core;

import cn.dev33.satoken.exception.NotLoginException;
import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.constants.GeneralApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * iw-eat服务异常处理拦截器
 *
 * @author wray
 * @since 2024/4/30
 */
@RestControllerAdvice
@Slf4j
@Order(-1)
public class EatExceptionHandlerInterceptor {

    @ExceptionHandler(NotLoginException.class)
    public GeneralResponse<?> authExceptionHandler(NotLoginException e) {
        return new GeneralResponse<>(GeneralApiCode.UNAUTHORIZED);
    }
}
