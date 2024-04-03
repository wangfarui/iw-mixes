package com.itwray.iw.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置
 *
 * @author wray
 * @since 2024/3/2
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${iw.version}")
    private String version;

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(this.getApplicationName() + " API")
                        .description(this.getApplicationName() + " service api.")
                        .version(version)
                        .license(new License().name("SpringDoc").url("http://springdoc.org"))
                );
    }

    public String getApplicationName() {
        return applicationName.toUpperCase();
    }
}
