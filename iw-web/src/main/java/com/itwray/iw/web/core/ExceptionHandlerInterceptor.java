package com.itwray.iw.web.core;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.IwException;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.FeignClientException;
import com.itwray.iw.web.exception.IwWebException;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
@Order(0)
public class ExceptionHandlerInterceptor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GeneralResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        return new GeneralResponse<>(GeneralApiCode.INTERNAL_SERVER_ERROR.getCode(), defaultMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public GeneralResponse<?> httpRequestMethodNotSupportedExceptionHandler() {
        return new GeneralResponse<>(GeneralApiCode.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizedException.class)
    public GeneralResponse<?> authExceptionHandler(AuthorizedException authorizedException) {
        return new GeneralResponse<>(GeneralApiCode.UNAUTHORIZED.getCode(), authorizedException.getMessage());
    }

    @ExceptionHandler(FeignClientException.class)
    public GeneralResponse<?> feignClientExceptionHandler(FeignClientException feignClientException) {
        log.error("[IW Feign异常]" + feignClientException.getMessage(), feignClientException);
        return new GeneralResponse<>(feignClientException.getCode(), feignClientException.getMessage());
    }

    @ExceptionHandler(DecodeException.class)
    public GeneralResponse<?> decodeExceptionHandler(DecodeException decodeException) {
        if (decodeException.getCause() instanceof FeignClientException feignClientException) {
            log.error("[IW Feign异常]" + feignClientException.getMessage(), feignClientException);
            return new GeneralResponse<>(feignClientException.getCode(), feignClientException.getMessage());
        }
        log.error("[IW Decode异常]" + decodeException.getMessage(), decodeException);
        return GeneralResponse.fail();
    }

    @ExceptionHandler(IwWebException.class)
    public GeneralResponse<?> iwWebExceptionHandler(IwWebException iwWebException) {
        log.error("[IW Web异常]" + iwWebException.getMessage(), iwWebException);
        return GeneralResponse.fail(iwWebException.getMessage());
    }

    @ExceptionHandler(IwException.class)
    public GeneralResponse<?> iwExceptionHandler(IwException iwException) {
        log.error("[IW异常]" + iwException.getMessage(), iwException);
        return GeneralResponse.fail();
    }

    @ExceptionHandler(Throwable.class)
    public GeneralResponse<?> defaultExceptionHandler(Throwable e) {
        log.error("系统异常", e);
        return GeneralResponse.fail();
    }
}
