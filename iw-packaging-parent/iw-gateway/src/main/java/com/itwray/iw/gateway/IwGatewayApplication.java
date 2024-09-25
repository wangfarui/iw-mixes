package com.itwray.iw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * IW Gateway 应用程序
 *
 * @author wray
 * @since 2024/7/16
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IwGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwGatewayApplication.class);
    }
}
