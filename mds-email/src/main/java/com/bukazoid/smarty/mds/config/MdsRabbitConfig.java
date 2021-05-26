package com.bukazoid.smarty.mds.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bukazoid.smarty.dto.SmartyQueries;

@Configuration
public class MdsRabbitConfig {

	public final static String MDS = "email";
	public final static String QUERY_NAME = SmartyQueries.MDS_SEND_BASE + MDS;

	@Bean
	public TopicExchange topicExchange() {
		TopicExchange exchange = new TopicExchange(SmartyQueries.EXCHANGE_NAME);
		return exchange;
	}

	/*
	 * message query
	 */
	@Bean
	public Queue messageQueue() {
		return new Queue(QUERY_NAME);
	}

	@Bean
	public Binding bindingBinding() {
		return BindingBuilder.bind(messageQueue()).to(topicExchange()).with(QUERY_NAME);
	}
}
