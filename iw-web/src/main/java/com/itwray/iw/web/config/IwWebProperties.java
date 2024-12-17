package com.itwray.iw.web.config;

import com.itwray.iw.web.model.enums.RuntimeEnvironmentEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * IW Web 属性配置
 *
 * @author wray
 * @since 2024/4/15
 */
@ConfigurationProperties(prefix = "iw.web")
@Validated
@Data
public class IwWebProperties {

    /**
     * 服务接口
     */
    @NotNull(message = "Web服务 API 不能为空")
    private Api api;

    /**
     * 服务运行环境
     */
    private RuntimeEnvironmentEnum env = RuntimeEnvironmentEnum.DEV;

    @Data
    @Valid
    public static class Api {

        @NotEmpty(message = "API 前缀不能为空")
        private String prefix;
    }
}
