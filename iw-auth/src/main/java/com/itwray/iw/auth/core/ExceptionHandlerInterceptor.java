package com.itwray.iw.auth.core;

import cn.hutool.json.JSONUtil;
import com.itwray.iw.common.ApiCode;
import com.itwray.iw.common.GeneralApiCode;
import com.itwray.iw.common.GeneralResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 异常处理拦截器
 *
 * @author wray
 * @since 2024/3/5
 */
public class ExceptionHandlerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        if (ex == null) {
            return;
        }

        GeneralApiCode serverError = GeneralApiCode.SERVER_ERROR;
        response.setStatus(serverError.getCode());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("utf-8");

        GeneralResponse<Object> generalResponse = new GeneralResponse<>();
        generalResponse.setCode(serverError.getCode());
        if (supportThrowException(ex)) {
            generalResponse.setMessage(ex.getMessage());
        } else {
            generalResponse.setMessage(serverError.getMessage());
        }

        response.getWriter().write(JSONUtil.toJsonStr(generalResponse));
    }

    private boolean supportThrowException(Exception e) {
        if (e instanceof ApiCode) {
            return true;
        }
        if (e instanceof BindValidationException) {
            return true;
        }
        return false;
    }
}
