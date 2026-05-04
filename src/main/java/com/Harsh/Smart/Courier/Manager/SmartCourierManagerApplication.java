package com.Harsh.Smart.Courier.Manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartCourierManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCourierManagerApplication.class, args);
	}

}
