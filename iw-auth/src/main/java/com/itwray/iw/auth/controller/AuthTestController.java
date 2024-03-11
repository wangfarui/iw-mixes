package com.itwray.iw.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 授权服务的测试接口
 *
 * @author wray
 * @since 2024/3/11
 */
@RestController
public class AuthTestController {

    @GetMapping("/hello")
    public String hello(Authentication authentication, Principal principal, HttpServletRequest request) {
        System.out.println("authentication: " + authentication);
        System.out.println("principal: " + principal);
        Principal userPrincipal = request.getUserPrincipal();
        System.out.println("request.getRemoteUser(): " + request.getRemoteUser());
        return "hello " + userPrincipal.getName();
    }
}
