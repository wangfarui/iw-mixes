package com.itwray.iw.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 阿里云属性配置
 *
 * @author wray
 * @since 2025/4/23
 */
@ConfigurationProperties(prefix = "aliyun")
@RefreshScope
@Data
public class IwAliyunProperties {

    /**
     * 短信服务
     */
    private SMS sms;

    /**
     * 文件服务
     */
    private OSS oss;

    /**
     * 邮件服务
     */
    private Email email;

    @Data
    public static class SMS {

        private String accessKeyId;

        private String accessKeySecret;

        /**
         * 签名名称
         */
        private String signName;

        /**
         * 模板CODE
         */
        private String templateCode;
    }

    @Data
    public static class OSS {

        private String accessKeyId;

        private String accessKeySecret;

        /**
         * 文件地址前缀
         */
        private String baseUrl;

        /**
         * 文件上传的endpoint
         */
        private String endpoint;

        /**
         * 文件上传的bucket
         */
        private String bucketName;

        /**
         * 文件上传的目录
         */
        private String uploadParentDir;
    }

    @Data
    public static class Email {

        private String accessKeyId;

        private String accessKeySecret;

        /**
         * 发件人的发信地址
         */
        private String accountName;

        /**
         * 发件人别名
         */
        private String fromAlias = "Wray";
    }
}
