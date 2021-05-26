package com.bukazoid.smarty.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.bukazoid.smarty.domain.store.DeviceSensor;

public interface SensorRepository extends CrudRepository<DeviceSensor, UUID> {
	DeviceSensor findByNameAndDeviceId(String name, UUID deviceId);

	DeviceSensor findByNameAndDeviceNameAndDeviceProvider(String name, String device, String provider);
}