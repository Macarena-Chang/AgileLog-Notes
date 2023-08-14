package com.macarenachang.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.macarenachang.authservice.controller" , "com.macarenachang.authservice.service", "com.macarenachang.authservice.dao", "com.macarenachang.authservice.config"})
@EntityScan(basePackages = {"com.macarenachang.authservice.model"})
@EnableJpaRepositories(basePackages= {"com.macarenachang.authservice.dao"})
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
