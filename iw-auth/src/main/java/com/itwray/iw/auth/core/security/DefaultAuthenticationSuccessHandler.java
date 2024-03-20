package com.itwray.iw.auth.core.security;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", 200);
        resp.put("msg", "登录成功!");
        // 认证成功之后返回json字符串
        response.getWriter().write(JSONUtil.toJsonStr(resp));
    }
}