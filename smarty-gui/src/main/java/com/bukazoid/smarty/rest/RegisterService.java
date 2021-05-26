package com.bukazoid.smarty.rest;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.repo.SensorRepository;
import com.bukazoid.smarty.service.PlatformTools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegisterService {
	private final PlatformTools tools;
	private final SensorRepository sensorRepo;

	@RequestMapping(value = "/api/monitoron/{id}", method = RequestMethod.GET)
	public Boolean register(@PathVariable("id") String id) {
		log.info("try to register: {}", id);
		Optional<DeviceSensor> sensor = sensorRepo.findById(UUID.fromString(id));
		sensor.ifPresent(s -> {
			s.setMonitored(true);
			sensorRepo.save(s);
		});
		return tools.register(id);
	}

	@RequestMapping(value = "/api/monitoroff/{id}", method = RequestMethod.GET)
	public Boolean unregister(@PathVariable("id") String id) {
		log.info("try to unregister: {}", id);
		Optional<DeviceSensor> sensor = sensorRepo.findById(UUID.fromString(id));
		sensor.ifPresent(s -> {
			s.setMonitored(false);
			sensorRepo.save(s);
		});
		return tools.unregister(id);
	}
}
