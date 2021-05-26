package com.bukazoid.smarty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@SpringBootApplication
public class SmartyGui {

	public static void main(String[] args) {
		SpringApplication.run(SmartyGui.class, args);
	}
}
