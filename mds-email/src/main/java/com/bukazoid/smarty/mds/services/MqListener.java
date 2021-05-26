package com.bukazoid.smarty.mds.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.dto.mds.MdsAlert;
import com.bukazoid.smarty.mds.config.MdsRabbitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MqListener {
	private final ObjectMapper mapper;
	private final EMailSender sender;

	@RabbitListener(queues = MdsRabbitConfig.QUERY_NAME)
	public void processMessage(String message) throws JsonProcessingException {
		try {
			MdsAlert aMessage = mapper.readValue(message, MdsAlert.class);

			if (!aMessage.getDeliverySystem().equals(MdsRabbitConfig.MDS)) {
				log.error("incorrect mds: {}, expected: {}", aMessage.getDeliverySystem(), MdsRabbitConfig.MDS);
				return;
			}

			log.info("may send email now");
			sender.send(aMessage.getTarget(), "service message", aMessage.getMessage());
		} catch (Exception e) {
			log.error("can't read message", e);
		}
	}
}