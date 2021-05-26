package com.bukazoid.smarty.plant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MqttPlantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttPlantApplication.class, args);
	}

}