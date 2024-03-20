package com.itwray.iw.auth.core.security;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", 200);
        resp.put("msg", "注销成功!");
        // 退出登录之后返回json字符串
        response.getWriter().write(JSONUtil.toJsonStr(resp));
    }
}