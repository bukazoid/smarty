package com.bukazoid.smarty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DbInitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbInitApplication.class, args);
	}

}