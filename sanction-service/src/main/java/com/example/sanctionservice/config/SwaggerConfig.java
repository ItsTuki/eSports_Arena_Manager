package com.example.sanctionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("sanction-service API")
                .version("1.0.0")
                .description("Documentacion OpenAPI para sanciones y bloqueos de inscripcion."));
    }
}
