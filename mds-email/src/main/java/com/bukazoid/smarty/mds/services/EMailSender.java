package com.bukazoid.smarty.mds.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.mds.config.AppProps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EMailSender {
	private final JavaMailSender mailSender;
	private final AppProps appProps;

	public void send(String to, String subject, String text) {
		log.info("send message {}", text);
		if (appProps.isEmulate()) {
			log.info("EMULATE MODE ON\nemail message to: {} subject: {} \ntext: {} ", to, subject, text);
			return;
		}

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setFrom(appProps.getSenderEmail());
		mailMessage.setSubject(subject);
		mailMessage.setText(text);

		mailSender.send(mailMessage);
	}
}
