package com.example.tournamentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("tournament-service API")
                .version("1.0.0")
                .description("Documentacion OpenAPI para torneos, fechas, cupos y estados."));
    }
}
