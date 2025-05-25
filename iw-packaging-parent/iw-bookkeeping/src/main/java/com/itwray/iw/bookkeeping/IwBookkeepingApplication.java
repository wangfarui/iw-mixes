package com.itwray.iw.bookkeeping;

import com.itwray.iw.eat.IwEatApplication;
import com.itwray.iw.points.IwPointsApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * IW记账服务启动类
 *
 * @author wray
 * @since 2024/7/5
 */
@SpringBootApplication
@Import({IwPointsApplication.class, IwEatApplication.class})
@EnableDiscoveryClient
@EnableScheduling
public class IwBookkeepingApplication {

    public static void main(String[] args) {
        SpringApplication.run(IwBookkeepingApplication.class);
    }
}
