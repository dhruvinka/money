package com.example.expenssManeger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpenssManegerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenssManegerApplication.class, args);
	}

}
