package com.itwray.iw.auth.core.security;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", 500);
        resp.put("msg", exception.getMessage());
        // 认证失败之后返回json字符串
        response.getWriter().write(JSONUtil.toJsonStr(resp));
    }
}