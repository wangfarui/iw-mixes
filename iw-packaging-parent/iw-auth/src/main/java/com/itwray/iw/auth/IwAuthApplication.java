package com.itwray.iw.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * IW认证服务应用程序
 *
 * @author wray
 * @since 2024/3/2
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class IwAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwAuthApplication.class);
    }
}
