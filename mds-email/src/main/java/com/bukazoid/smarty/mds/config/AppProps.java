package com.bukazoid.smarty.mds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("app")
public class AppProps {
	private String senderEmail;
	private boolean emulate;
}
