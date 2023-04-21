package com.example.secondClientService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SecondClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondClientServiceApplication.class, args);
    }

}
