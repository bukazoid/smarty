package com.bukazoid.smarty.mds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableMapRepositories
@EnableScheduling
@SpringBootApplication
public class MessageProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageProcessorApplication.class, args);
	}

}