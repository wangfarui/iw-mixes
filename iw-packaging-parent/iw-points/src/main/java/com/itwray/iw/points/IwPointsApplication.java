package com.itwray.iw.points;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * IW积分服务启动类
 *
 * @author wray
 * @since 2024/9/25
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IwPointsApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwPointsApplication.class);
    }
}
