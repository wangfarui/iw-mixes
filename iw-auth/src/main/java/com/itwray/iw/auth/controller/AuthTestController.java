package com.itwray.iw.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 认证服务的测试接口
 *
 * @author wray
 * @since 2024/3/11
 */
@RestController
public class AuthTestController {

    @GetMapping("/hello")
    public String hello(Authentication authentication, Principal principal, HttpServletRequest request) {
        if (authentication != null) {
            System.out.println("authentication: " + authentication);
        }
        if (principal != null) {
            System.out.println("principal: " + principal);
        }
        System.out.println("request.getRemoteUser(): " + request.getRemoteUser());
        Principal userPrincipal = request.getUserPrincipal();
        String name = userPrincipal != null ? userPrincipal.getName() : "null";
        return "hello " + name;
    }

    @GetMapping("/hello2")
    public DefaultOAuth2User hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((DefaultOAuth2User) authentication.getPrincipal());
    }
}
