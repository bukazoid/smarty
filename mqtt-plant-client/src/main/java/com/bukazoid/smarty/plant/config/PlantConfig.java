package com.bukazoid.smarty.plant.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bukazoid.smarty.dto.SmartyQueries;

@Configuration
public class PlantConfig {

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(SmartyQueries.EXCHANGE_NAME);
		return rabbitTemplate;
	}

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(SmartyQueries.EXCHANGE_NAME);
	}

}
