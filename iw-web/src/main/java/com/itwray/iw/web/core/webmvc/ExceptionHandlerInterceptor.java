package com.itwray.iw.web.core.webmvc;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.common.IwException;
import com.itwray.iw.common.constants.GeneralApiCode;
import com.itwray.iw.common.utils.ExceptionUtils;
import com.itwray.iw.web.exception.AuthorizedException;
import com.itwray.iw.web.exception.FeignClientException;
import com.itwray.iw.web.exception.IwWebException;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
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

    @ExceptionHandler(IwWebException.class)
    public GeneralResponse<?> iwWebExceptionHandler(IwWebException iwWebException) {
        log.error("[IW Web异常]" + iwWebException.getMessage(), iwWebException);
        return GeneralResponse.fail(iwWebException.getMessage());
    }

    @ExceptionHandler({MyBatisSystemException.class, PersistenceException.class})
    public GeneralResponse<?> mybatisExceptionHandler(RuntimeException e) {
        IwException iwException = ExceptionUtils.extractIwException(e);
        if (iwException != null) {
            return this.wrapResponseByIwException(iwException);
        }
        log.error("[MyBatis异常]" + e.getMessage(), e);
        return GeneralResponse.fail();
    }

    @ExceptionHandler({FeignClientException.class, DecodeException.class})
    public GeneralResponse<?> feignExceptionHandler(RuntimeException e) {
        IwException iwException = ExceptionUtils.extractIwException(e);
        if (iwException != null) {
            return this.wrapResponseByIwException(iwException);
        }
        log.error("[Feign异常]" + e.getMessage(), e);
        return GeneralResponse.fail();
    }

    /**
     * 默认异常处理器，默认返回"系统异常"
     * <p>最后异常处理中，不再判断异常是否来源于内部异常，因为有些内部异常信息可能也不希望暴露到外部中。
     */
    @ExceptionHandler(Throwable.class)
    public GeneralResponse<?> defaultExceptionHandler(Throwable e) {
        log.error("系统异常", e);
        return GeneralResponse.fail();
    }

    /**
     * 根据内部异常信息，包装响应对象
     * <p>只应该针对编码中可预测的内部异常信息进行包装！！！
     */
    private GeneralResponse<?> wrapResponseByIwException(IwException iwException) {
        log.error("[IW异常]" + iwException.getMessage(), iwException);
        return new GeneralResponse<>(iwException.getCode(), iwException.getMessage());
    }
}
