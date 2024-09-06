package com.itwray.iw.starter.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * IW Redis 属性配置
 *
 * @author wray
 * @since 2024/9/6
 */
@ConfigurationProperties(prefix = "iw.redis")
@Data
public class IwRedisProperties {

    /**
     * 数据库
     */
    private int database = 0;

    /**
     * Host
     */
    private String host = "localhost";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 端口
     */
    private int port = 6379;
}
