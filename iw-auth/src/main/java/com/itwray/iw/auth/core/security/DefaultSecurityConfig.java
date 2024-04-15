package com.itwray.iw.auth.core.security;

import com.itwray.iw.auth.dao.AuthPersistentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 默认 SpringSecurity 配置
 *
 * @author wray
 * @since 2024/3/8
 */
@Configuration
public class DefaultSecurityConfig implements ApplicationContextAware {

    private AuthenticationConfiguration authenticationConfiguration;

    private ApplicationContext applicationContext;

    @Autowired
    public void setAuthenticationConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        // 数据准备 提前暴露可能存在的异常
        AuthPersistentDao authPersistentDao = this.getBeanOfNonNull(AuthPersistentDao.class);
        LoginCaptchaFilter loginCaptchaFilter = this.loginCaptchaFilter();

        return http.authorizeHttpRequests(t -> t
                        // 静态资源
                        .requestMatchers("/css/**", "/js/**")
                        .permitAll()
                        // swagger ui
                        .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // 登录、注册接口
                        .requestMatchers("/login/*", "/register/*")
                        .permitAll()
                        // oauth2 接口
                        .requestMatchers("/login/oauth2/code/*")
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
                        .logoutSuccessUrl("/login.html")
                )
                .rememberMe(t -> t.tokenRepository(new DefaultPersistentTokenRepository(authPersistentDao)))
                .oauth2Login(t ->
                        // 登录成功默认强制跳转到index.html
                        t.defaultSuccessUrl("/index.html", true)
                        // 使用默认的登录处理url规则
                        .loginProcessingUrl("/login/oauth2/code/*")
                )
                .addFilterBefore(loginCaptchaFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    public AuthenticationManager getAuthenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    public <T> T getBeanOfNonNull(Class<T> beanClazz) {
        return this.applicationContext.getBean(beanClazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 实例化登录验证码过滤器
     *
     * @return LoginCaptchaFilter
     * @throws Exception from this.getAuthenticationManager()
     */
    private LoginCaptchaFilter loginCaptchaFilter() throws Exception {
        LoginCaptchaFilter loginCaptchaFilter = new LoginCaptchaFilter(this.getAuthenticationManager());
        // 验证码认证成功后，不做任何处理操作
        loginCaptchaFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
        }));
        // 验证码认证失败后，回填 Response 对象
        loginCaptchaFilter.setAuthenticationFailureHandler(new DefaultAuthenticationFailureHandler());
        return loginCaptchaFilter;
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
