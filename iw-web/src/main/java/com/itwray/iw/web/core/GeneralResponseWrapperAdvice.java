package com.itwray.iw.web.core;

import com.itwray.iw.common.GeneralResponse;
import com.itwray.iw.web.annotation.SkipWrapper;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * {@link GeneralResponse} 包装器Advice
 *
 * @author wray
 * @since 2024/3/5
 */
@RestControllerAdvice
public class GeneralResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private static final Class<?> WRAPPER_CLASS = GeneralResponse.class;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 判断当前Controller类是否携带SkipWrapper注解
        if (AnnotationUtils.getAnnotation(returnType.getDeclaringClass(), SkipWrapper.class) != null) {
            return false;
        }

        // 判断当前接口请求方法是否携带SkipWrapper注解
        if (returnType.getExecutable() instanceof Method method) {
            // 不递归查找接口和父类方法
            return AnnotationUtils.getAnnotation(method, SkipWrapper.class) == null;
        }

        // 默认支持封装
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 只对 application/json 数据进行封装
        if (!MediaType.APPLICATION_JSON.toString().equals(selectedContentType.toString())) {
            return body;
        }

        // 跳过符合规则的uri
        if (this.isSkipUri(request.getURI().getPath())) {
            return body;
        }

        // 封装响应对象
        GeneralResponse<Object> baseResponse;
        if (returnType.getParameterType().isAssignableFrom(WRAPPER_CLASS)) {
            baseResponse = (GeneralResponse<Object>) body;
            if (baseResponse == null) {
                baseResponse = GeneralResponse.success();
            }
        } else {
            if (isBasicError(returnType.getDeclaringClass())) {
                baseResponse = GeneralResponse.fail();
                baseResponse.setData(body);
            } else {
                baseResponse = GeneralResponse.success(body);
            }
        }
        return baseResponse;
    }

    private boolean isSkipUri(String uri) {
        return uri.contains("/v3/api-docs");
    }

    private boolean isBasicError(Class<?> declaringClass) {
        return declaringClass == BasicErrorController.class;
    }
}
