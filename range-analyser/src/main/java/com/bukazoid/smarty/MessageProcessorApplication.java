package com.bukazoid.smarty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableMapRepositories
@EnableScheduling
@SpringBootApplication
public class MessageProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageProcessorApplication.class, args);
	}

}