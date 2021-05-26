package com.bukazoid.smarty.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bukazoid.smarty.service.PlatformTools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UnregisterService {
	private final PlatformTools tools;

	@RequestMapping(value = "/api/unregister/{id}", method = RequestMethod.GET)
	public Boolean unregister(@PathVariable("id") String id) {
		// XXX: maybe should be call to message-processor
		log.info("try to unregister: {}", id);

		return tools.unregister(id);
	}
}
