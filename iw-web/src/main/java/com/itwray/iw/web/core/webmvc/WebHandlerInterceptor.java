package com.itwray.iw.web.core.webmvc;

import com.itwray.iw.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Web请求拦截器
 *
 * @author wray
 * @since 2024/9/9
 */
public class WebHandlerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理每次请求中的用户id
        UserUtils.removeUserId();
    }
}
