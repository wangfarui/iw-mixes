package com.itwray.iw.oauth2.authorization.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OAuth2 授权服务器Application
 *
 * @author wray
 * @since 2024/4/11
 */
@SpringBootApplication
public class IwOAuth2AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwOAuth2AuthorizationServerApplication.class);
    }
}
