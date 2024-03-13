package com.itwray.iw.auth.core;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        return http.authorizeHttpRequests(t -> t.requestMatchers("/hello", "/*/captcha.jpg", "*.css", "*.js")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
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

    @Bean
    public AuthenticationProvider captchaDaoAuthenticationProvider(UserDetailsService userDetailsService) {
        CaptchaDaoAuthenticationProvider provider = new CaptchaDaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    static class CaptchaDaoAuthenticationProvider extends DaoAuthenticationProvider {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                String captcha = request.getParameter("captcha");
                String captchaSession = (String) request.getSession().getAttribute("captcha");
                // 判断验证码
                if (captcha == null || captchaSession == null || !captchaSession.equals(captcha.trim().toLowerCase())) {
                    throw new AuthenticationServiceException("验证码输入错误");
                }
            }

            return super.authenticate(authentication);
        }
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
            resp.put("msg", exception.getMessage());
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
