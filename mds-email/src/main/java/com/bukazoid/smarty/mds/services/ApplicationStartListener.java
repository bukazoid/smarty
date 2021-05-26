package com.bukazoid.smarty.mds.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bukazoid.smarty.dto.SmartyQueries;
import com.bukazoid.smarty.dto.mds.MdsParameterDefinition;
import com.bukazoid.smarty.dto.mds.MdsRegister;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
class ApplicationStartListener implements ApplicationListener<ApplicationReadyEvent> {
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	private final EMailSender sender;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("ApplicationListener#onApplicationEvent()");
		sendRegisterMessage();

		sendTestMailMessage();
	}

	public void sendRegisterMessage() {
		try {
			MdsRegister register = new MdsRegister();
			register.setName("email");
			register.getParams().add(new MdsParameterDefinition("e-mail address", null, true));
			rabbitTemplate.convertAndSend(SmartyQueries.MDS_REGISTER, objectMapper.writeValueAsString(register));
			log.info("sent");
		} catch (Throwable th) {
			log.error("can't send", th);
		}
	}

	// XXX: remove it when everything works fine
	public void sendTestMailMessage() {
		// sender.send("pavel.p.yakimov@gmail.com", "service up", "application start at " + new Date().toString());
	}
}
