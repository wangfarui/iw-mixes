package com.itwray.iw.eat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * IW点餐服务应用程序
 *
 * @author wray
 * @since 2024/4/22
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IwEatApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwEatApplication.class);
    }
}
