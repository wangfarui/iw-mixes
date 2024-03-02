package com.itwray.iw.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * IW授权服务应用程序
 *
 * @author wangfarui
 * @since 2024/3/2
 */
@EnableOpenApi
@SpringBootApplication
public class IwAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwAuthApplication.class);
    }
}
