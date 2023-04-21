package com.example.clientservice.controller;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

import com.example.clientservice.model.Foo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @GetMapping("foos")
    public String hello() {
        return "hello from client service !";

    }

    @GetMapping("foos/{id}")
    public Foo findById(@PathVariable long id, HttpServletRequest req, HttpServletResponse res) {
        System.out.println(req.getAuthType());
        System.out.println(req.getHeaderNames().asIterator().next());
        System.out.println(req.getMethod());
        System.out.println(req.getLocalPort());

        System.out.println(res.getStatus());
        System.out.println(res.getHeaderNames());
        System.out.println(res.getContentType());

        return new Foo(id, randomAlphabetic(4));
    }

    //    with Filters:
    @GetMapping("/filter/{id}")
    public Foo findWithFilter(@PathVariable long id,
                                            HttpServletRequest req,
                                            HttpServletResponse res) {
        if (req.getHeader("Test") != null) {
	   res.addHeader("Test", req.getHeader("Test"));
        }

        System.out.println(req.getHeaderNames().asIterator().next());
        System.out.println(res.getHeaderNames());
        System.out.println(res.getHeader("Test"));

        return new Foo(id, randomAlphabetic(4));
    }


}
