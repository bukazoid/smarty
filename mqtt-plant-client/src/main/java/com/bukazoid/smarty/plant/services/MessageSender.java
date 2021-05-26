package com.bukazoid.smarty.plant.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.dto.SmartyQueries;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * sends message into rabbitMQ
 * @author frozen
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MessageSender {
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	public void sendMessage(Object message) {
		try {
			log.info("send message: {}", message);
			rabbitTemplate.convertAndSend(SmartyQueries.ROUTE_KEY_REQULAR, objectMapper.writeValueAsString(message));
			// rabbitTemplate.convertAndSend(PlantConfig.PROCESSED_MESSAGES, message); //jsonb?
			// rabbitTemplate.send()
			log.info("sent");
		} catch (Throwable th) {
			log.error("can't send", th);
		}
	}
}
