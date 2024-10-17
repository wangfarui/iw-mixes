package com.itwray.iw.web.core.feign;

import com.itwray.iw.web.core.webmvc.GeneralResponseWrapperAdvice;
import com.itwray.iw.web.utils.UserUtils;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置类
 * <p>
 * 要求所有Feign接口返回对象都要经过 {@link GeneralResponseWrapperAdvice}
 * </p>
 *
 * @author wray
 * @see FeignClientsConfiguration
 * @since 2024/9/29
 */
@Configuration
@EnableFeignClients(basePackages = "com.itwray.iw.*.client")
public class FeignConfiguration {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignConfiguration(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    /**
     * feign解码器
     *
     * @param customizers HTTP消息转换器自定义器
     * @return 默认解码器
     * @see FeignClientsConfiguration#feignDecoder(ObjectProvider)
     */
    @Bean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        return new GeneralResponseDecoder(new OptionalDecoder(new SpringDecoder(messageConverters, customizers)));
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 从当前请求上下文获取token
            String token = UserUtils.getToken();
            if (token != null) {
                // 将 token 添加到请求头中
                requestTemplate.header(UserUtils.TOKEN_HEADER, token);
            }
        };
    }
}
