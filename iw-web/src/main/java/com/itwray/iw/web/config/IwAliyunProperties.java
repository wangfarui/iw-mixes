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

    /**
     * 语音识别服务
     */
    private Asr asr;

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

    @Data
    public static class Asr {

        /**
         * 阿里云 AccessKey ID
         */
        private String accessKeyId;

        /**
         * 阿里云 AccessKey Secret
         */
        private String accessKeySecret;

        /**
         * 智能语音交互 AppKey
         */
        private String appKey;

        /**
         * 一句话识别网关
         */
        private String gatewayUrl = "https://nls-gateway-cn-shanghai.aliyuncs.com/stream/v1/asr";

        /**
         * 地域
         */
        private String regionId = "cn-shanghai";

        /**
         * Token 域名
         */
        private String tokenDomain = "nls-meta.cn-shanghai.aliyuncs.com";

        /**
         * Token 版本号
         */
        private String tokenVersion = "2019-02-28";

        /**
         * Token Action
         */
        private String tokenAction = "CreateToken";

        /**
         * 默认音频格式
         */
        private String defaultFormat = "mp3";

        /**
         * 默认采样率
         */
        private Integer defaultSampleRate = 16000;

        /**
         * 是否启用标点预测
         */
        private Boolean enablePunctuationPrediction = Boolean.TRUE;

        /**
         * 是否启用逆文本正规化
         */
        private Boolean enableInverseTextNormalization = Boolean.TRUE;

        /**
         * 是否启用语音端点检测
         */
        private Boolean enableVoiceDetection = Boolean.FALSE;

        /**
         * 是否过滤语气词
         */
        private Boolean disfluency = Boolean.FALSE;

        /**
         * 连接超时时间
         */
        private Integer connectTimeoutMillis = 5000;

        /**
         * 读取超时时间
         */
        private Integer readTimeoutMillis = 20000;

        /**
         * Token 提前刷新秒数
         */
        private Integer tokenRefreshBeforeSeconds = 300;

        /**
         * ffmpeg命令路径
         */
        private String ffmpegCommand = "ffmpeg";

        /**
         * 音频转码超时时间(秒)
         */
        private Integer ffmpegTimeoutSeconds = 20;
    }
}
