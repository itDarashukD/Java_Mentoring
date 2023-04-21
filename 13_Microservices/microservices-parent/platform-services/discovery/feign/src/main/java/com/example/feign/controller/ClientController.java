package com.example.feign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ClientController {


    @Autowired
    GreetingsFeign greetingsFeign;

    @RequestMapping("/get-greeting")
    public String greeting() {
        return greetingsFeign.greeting();
    }

}
