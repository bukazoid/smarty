package com.bukazoid.smarty.mds.config;

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
public class MdsDispatcherConfig {

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbit = new RabbitTemplate(connectionFactory);
		rabbit.setExchange(SmartyQueries.EXCHANGE_NAME);
		return rabbit;
	}

	@Bean
	public TopicExchange topicExchange() {
		TopicExchange exchange = new TopicExchange(SmartyQueries.EXCHANGE_NAME);
		return exchange;
	}

	/*
	 * register query
	 */
	@Bean
	public Binding registerBinding() {
		return BindingBuilder.bind(registerQueue()).to(topicExchange()).with(SmartyQueries.MDS_REGISTER);
	}

	@Bean
	public Queue registerQueue() {
		return new Queue(SmartyQueries.MDS_REGISTER);
	}

	/*
	 * alarm query
	 */
	@Bean
	public Binding alertBinding() {
		return BindingBuilder.bind(alertQueue()).to(topicExchange()).with(SmartyQueries.MDS_ALERT);
	}

	@Bean
	public Queue alertQueue() {
		return new Queue(SmartyQueries.MDS_ALERT);
	}
}
