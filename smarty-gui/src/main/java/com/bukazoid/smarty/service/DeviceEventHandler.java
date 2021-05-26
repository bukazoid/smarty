package com.bukazoid.smarty.service;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.store.Device;
import com.bukazoid.smarty.repo.redis.ValuesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RepositoryEventHandler(Device.class)
@RequiredArgsConstructor
public class DeviceEventHandler {
	private final ValuesRepository valuesRepo;

	@HandleBeforeCreate
	public void handleUserCreate(Device device) {
		log.info("before create");

	}

	@HandleBeforeSave
	public void handleUserUpdate(Device device) {
		log.info("before update, getHumanName: {}", device.getHumanName());
		// maybe separate thread would be better
		valuesRepo.findByDeviceId(device.getId().toString()).forEach(val -> {
			val.setDeviceHumanName(device.getHumanName());
			valuesRepo.save(val);
		});
	}
}