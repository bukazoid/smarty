package com.bukazoid.smarty.repo;

import java.util.List;
import java.util.UUID;

import com.bukazoid.smarty.domain.store.DeviceSensor;

public interface CustomDeviceSensorRepository {
	List<DeviceSensor> findByViewId(UUID viewId);
}
