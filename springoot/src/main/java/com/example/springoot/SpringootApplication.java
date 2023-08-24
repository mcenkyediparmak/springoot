package com.example.springoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringootApplication.class, args);
	}

}
