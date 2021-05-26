package com.bukazoid.smarty.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bukazoid.smarty.dto.SmartyQueries;

@Configuration
public class MessageProcessorConfig {

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(SmartyQueries.EXCHANGE_NAME);
	}

	@Bean
	public Binding importantActivityBinding() {
		return BindingBuilder.bind(messageProcessorQueue()).to(topicExchange()).with(SmartyQueries.ROUTE_KEY_ALL);
	}

	@Bean
	public Queue messageProcessorQueue() {
		return new Queue(SmartyQueries.QUEUE_MESSAGE_PROCESSOR);
	}
}
