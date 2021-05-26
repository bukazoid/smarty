package com.bukazoid.smarty.service;

import com.bukazoid.smarty.domain.redis.CurrentValue;
import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.dto.SensorValue;

public interface PlatformTools {

	/**
	 * XXX: optimize, it can be done in one request
	 * @param value
	 * @return
	 */
	DeviceSensor getSensor(SensorValue value);

	DeviceSensor getSensor(CurrentValue value);

	boolean unregister(String id);

	boolean register(String id);

}