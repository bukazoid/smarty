package com.bukazoid.smarty.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.bukazoid.smarty.domain.store.SensorData;

public interface DataRepository extends CrudRepository<SensorData, UUID> {
	
}