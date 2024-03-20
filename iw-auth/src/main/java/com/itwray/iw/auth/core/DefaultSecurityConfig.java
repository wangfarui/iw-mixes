package com.itwray.iw.auth.core;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public void setAuthenticationConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(t -> t
                        // 静态资源
                        .requestMatchers(HttpMethod.GET, "/css/**", "/js/**", "/*/captcha.jpg")
                        .permitAll()
                        // swagger ui
                        .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // 忽略认证接口
                        .requestMatchers("/hello")
                        .permitAll()
                        // 除以上请求外的所有请求都需要认证
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(t -> t.loginPage("/login.html")
                        .loginProcessingUrl("/doLogin")
                        .usernameParameter("uname")
                        .passwordParameter("passwd")
                        .successHandler(new DefaultAuthenticationSuccessHandler())
                        .failureHandler(new DefaultAuthenticationFailureHandler())
                        .permitAll()
                )
                .logout(t -> t.logoutUrl("/doLogout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(new DefaultLogoutSuccessHandler())
                )
                .addFilterBefore(loginCaptchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    public AuthenticationManager getAuthenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    private LoginCaptchaFilter loginCaptchaFilter() throws Exception {
        LoginCaptchaFilter loginCaptchaFilter = new LoginCaptchaFilter(this.getAuthenticationManager());
        // 验证码认证成功后，不做任何处理操作
        loginCaptchaFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
        }));
        // 验证码认证失败后，回填 Response 对象
        loginCaptchaFilter.setAuthenticationFailureHandler(new DefaultAuthenticationFailureHandler());
        return loginCaptchaFilter;
    }

    static class CaptchaAuthenticationToken extends AbstractAuthenticationToken {

        private final String captcha;

        public CaptchaAuthenticationToken(String captcha) {
            super(null);
            this.captcha = captcha;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        public String getCaptcha() {
            return captcha;
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

    /**
     * 登录验证码过滤器
     */
    private static class LoginCaptchaFilter extends AbstractAuthenticationProcessingFilter {

        public LoginCaptchaFilter(AuthenticationManager authenticationManager) {
            super(new AntPathRequestMatcher("/doLogin", "POST"), authenticationManager);
            super.setContinueChainBeforeSuccessfulAuthentication(true);
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            String captcha = request.getParameter("captcha");
            String captchaSession = (String) request.getSession().getAttribute("captcha");
            // 判断验证码
            if (captcha == null || captchaSession == null || !captchaSession.equals(captcha.trim().toLowerCase())) {
                throw new AuthenticationServiceException("验证码输入错误");
            }
            return new CaptchaAuthenticationToken(captcha);
        }
    }
}
