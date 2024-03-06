package com.itwray.iw.auth.core;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("IW-AUTH API")
                        .description("IW auth service api.")
                        .version("v0.0.1")
                        .license(new License().name("SpringDoc").url("http://springdoc.org"))
                );
    }
}
