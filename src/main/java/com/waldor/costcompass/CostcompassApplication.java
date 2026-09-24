package com.waldor.costcompass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CostcompassApplication {

	public static void main(String[] args) {
		SpringApplication.run(CostcompassApplication.class, args);
	}

}
