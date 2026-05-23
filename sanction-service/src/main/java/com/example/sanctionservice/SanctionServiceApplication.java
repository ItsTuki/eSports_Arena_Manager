package com.example.sanctionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SanctionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanctionServiceApplication.class, args);
    }

}
