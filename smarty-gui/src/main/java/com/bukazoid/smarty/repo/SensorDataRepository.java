package com.bukazoid.smarty.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bukazoid.smarty.domain.store.SensorData;

public interface SensorDataRepository extends JpaRepository<SensorData, UUID> {

}
