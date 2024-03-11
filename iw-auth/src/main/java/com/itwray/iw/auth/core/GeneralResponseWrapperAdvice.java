package com.itwray.iw.auth.core;

import com.itwray.iw.auth.model.GeneralResponse;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * {@link GeneralResponse} 包装器Advice
 *
 * @author wray
 * @since 2024/3/5
 */
@ControllerAdvice
public class GeneralResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private static final Class<?> WRAPPER_CLASS = GeneralResponse.class;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // TODO 暂时支持对所有返回类型做包装处理
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
