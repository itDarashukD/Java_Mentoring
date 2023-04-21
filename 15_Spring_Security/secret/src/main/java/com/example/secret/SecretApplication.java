package com.example.secret;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"com.example.secret"})
@MapperScan("com.example.secret.repository")
@SpringBootApplication
public class SecretApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecretApplication.class, args);
	}

}
