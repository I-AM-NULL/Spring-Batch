package com.demo.RestApiItemReader;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1")
public class SpringBatchRestApiItemReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchRestApiItemReaderApplication.class, args);
	}
	
	@GetMapping("/students")
	public List<StudentResponse> students()
	{
		return Arrays.asList(
				new StudentResponse(1L, "Randy", "Orton", "RandyOrton@gmail.com"),
				new StudentResponse(2L, "Jhon", "Cena", "JhonCena@gmail.com"),
				new StudentResponse(3L, "Orange", "Cassady", "OrangeCassady@gmail.com"));
	}
	
	

}
