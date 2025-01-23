package com.itwray.iw.bookkeeping;

import com.itwray.iw.eat.IwEatApplication;
import com.itwray.iw.points.IwPointsApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * IW记账服务启动类
 *
 * @author wray
 * @since 2024/7/5
 */
@SpringBootApplication(scanBasePackages = {"com.itwray.iw.bookkeeping", "com.itwray.iw.points", "com.itwray.iw.eat"})
@EnableDiscoveryClient
public class IwBookkeepingApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwBookkeepingApplication.class);
    }
}
