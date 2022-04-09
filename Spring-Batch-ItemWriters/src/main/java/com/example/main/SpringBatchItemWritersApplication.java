package com.example.main;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableBatchProcessing
@EnableAsync
public class SpringBatchItemWritersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchItemWritersApplication.class, args);
	}

}
