package com.bukazoid.smarty.mds.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.dto.SmartyQueries;
import com.bukazoid.smarty.dto.mds.MdsAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MdsAlertMessages {
	private final RabbitTemplate rabbit;
	private final ObjectMapper mapper;
	
	@RabbitListener(queues = SmartyQueries.MDS_ALERT)
	public void processMessage(String message) throws JsonProcessingException {
		try {
			log.info("MDS_ALERT_MESSAGE: {}", message);
			MdsAlert msg = mapper.readValue(message, MdsAlert.class);
			// redirect it

			log.info("redirect to: {}", SmartyQueries.MDS_SEND_BASE + msg.getDeliverySystem());
			rabbit.convertAndSend(SmartyQueries.MDS_SEND_BASE + msg.getDeliverySystem(), message);
		} catch (Exception e) {
			log.error("can't read message", e);
		}
	}
}