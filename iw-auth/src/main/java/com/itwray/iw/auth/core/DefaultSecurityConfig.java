package com.itwray.iw.auth.core;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认 SpringSecurity 配置
 *
 * @author wray
 * @since 2024/3/8
 */
@Configuration
public class DefaultSecurityConfig {

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(t -> t.anyRequest().authenticated())
                .formLogin(t -> t.loginPage("/login.html")
                        .loginProcessingUrl("/doLogin")
                        .successHandler(new DefaultAuthenticationSuccessHandler())
                        .failureHandler(new DefaultAuthenticationFailureHandler())
                        .usernameParameter("uname")
                        .passwordParameter("passwd")
                        .permitAll()
                )
                .logout(t -> t.logoutUrl("/doLogout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(new DefaultLogoutSuccessHandler())
                )
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    static class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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

    static class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
            Map<String, Object> resp = new HashMap<>();
            resp.put("status", 500);
            resp.put("msg", "登录失败, 用户名或密码错误!");
            // 认证失败之后返回json字符串
            response.getWriter().write(JSONUtil.toJsonStr(resp));
        }
    }

    static class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {

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
}
