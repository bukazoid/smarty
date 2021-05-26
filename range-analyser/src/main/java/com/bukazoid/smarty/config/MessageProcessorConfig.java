package com.bukazoid.smarty.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bukazoid.smarty.dto.SmartyQueries;

@Configuration
public class MessageProcessorConfig {

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(SmartyQueries.EXCHANGE_NAME);
		return rabbitTemplate;
	}

	@Bean
	public TopicExchange topicExchange() {
		TopicExchange exchange = new TopicExchange(SmartyQueries.EXCHANGE_NAME);
		return exchange;
	}

	@Bean
	public Binding rangeCheckerBinding() {
		return BindingBuilder.bind(messageProcessorQueue()).to(topicExchange()).with(SmartyQueries.ROUTE_KEY_REQULAR /*routing key*/ );
	}

	@Bean
	public Queue messageProcessorQueue() {
		return new Queue(SmartyQueries.QUEUE_RANGE_CHECKER /*queue name*/ );
	}
}