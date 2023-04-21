package com.example.two.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigPropertiesController {


    @GetMapping("/test")
    public String getPropertyValue() {

        return "it works";
    }


}
