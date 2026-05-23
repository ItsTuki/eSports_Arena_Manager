package com.example.prizeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PrizeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrizeServiceApplication.class, args);
    }

}
