package com.bukazoid.smarty.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bukazoid.smarty.domain.store.view.SensorProjection;

public interface ProjectionRepository extends JpaRepository<SensorProjection, UUID> {
	List<SensorProjection> findByViewId(UUID viewId);

	SensorProjection findByViewIdAndSensorId(UUID viewId, UUID sensorId);

	List<SensorProjection> findBySensorId(UUID sensorId);
}