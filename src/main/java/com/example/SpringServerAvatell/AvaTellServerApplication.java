package com.example.SpringServerAvatell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AvaTellServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvaTellServerApplication.class, args);

		System.out.println("http://localhost:8080");
	}
}