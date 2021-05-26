package com.bukazoid.smarty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("app")
public class AppProps {
	/**
	 * is secure(https) is required
	 */
	private boolean secure;
}
