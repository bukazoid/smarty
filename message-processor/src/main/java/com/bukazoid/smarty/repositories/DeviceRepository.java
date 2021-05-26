package com.bukazoid.smarty.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bukazoid.smarty.domain.store.Device;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
	Device findByNameAndProvider(String name, String provider);
}