package com.bukazoid.smarty.mds.services;

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
public class RabbitMqMessageSender {
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	public void sendRegisterMessage(Object message) {
		try {
			rabbitTemplate.convertAndSend(SmartyQueries.MDS_REGISTER, objectMapper.writeValueAsString(message));
			log.info("sent");
		} catch (Throwable th) {
			log.error("can't send", th);
		}
	}
}
