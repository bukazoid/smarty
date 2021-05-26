package com.bukazoid.smarty.mds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
	
@EnableScheduling
@SpringBootApplication
public class MdsEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdsEmailApplication.class, args);
	}

}