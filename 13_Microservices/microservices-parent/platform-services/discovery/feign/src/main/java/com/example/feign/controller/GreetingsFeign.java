package com.example.feign.controller;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "spring-cloud-eureka-client")
public interface GreetingsFeign {

    @GetMapping("/greeting")
    String greeting();

}
