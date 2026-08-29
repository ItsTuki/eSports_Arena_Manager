package com.example.rankingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ranking-service API")
                .version("1.0.0")
                .description("Documentacion OpenAPI para rankings, posiciones y estadisticas."));
    }
}
