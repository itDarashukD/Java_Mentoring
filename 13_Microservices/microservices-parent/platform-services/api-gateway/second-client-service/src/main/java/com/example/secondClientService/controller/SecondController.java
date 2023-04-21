package com.example.secondClientService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondController {


    @GetMapping("controller/hello")
    public String hello() {
        return "hello from second service !";
    }

    @GetMapping("find/{id}")
    public String findById(@PathVariable long id) {
        return "id of you operation is :" + id;
    }


}
