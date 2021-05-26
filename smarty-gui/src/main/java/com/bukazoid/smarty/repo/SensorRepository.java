package com.bukazoid.smarty.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bukazoid.smarty.domain.store.DeviceSensor;

@RepositoryRestResource(collectionResourceRel = "sensors", path = "sensors")
public interface SensorRepository extends CrudRepository<DeviceSensor, UUID>, CustomDeviceSensorRepository {
	DeviceSensor findByNameAndDeviceId(String name, UUID deviceId);
}