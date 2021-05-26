package com.bukazoid.smarty.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bukazoid.smarty.domain.store.view.SensorProjection;

@RepositoryRestResource(collectionResourceRel = "projections", path = "projections")
public interface ProjectionRepository extends JpaRepository<SensorProjection, UUID> {
	List<SensorProjection> findByViewId(UUID viewId);

	SensorProjection findByViewIdAndSensorId(UUID viewId, UUID sensorId);
}