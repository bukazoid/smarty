package com.bukazoid.smarty.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bukazoid.smarty.domain.store.DeviceSensor;

public interface DeviceSensorRepository extends JpaRepository<DeviceSensor, UUID> {

	DeviceSensor findByNameAndDeviceId(String name, UUID deviceId);

}
