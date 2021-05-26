package com.bukazoid.smarty.domain.redis;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.bukazoid.smarty.domain.store.Device;
import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.dto.SensorType;
import com.bukazoid.smarty.dto.SensorValue;

import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("values")
@Data
@NoArgsConstructor
public class CurrentValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1192962801187374770L;

	@Id
	private String id = UUID.randomUUID().toString();

	/**
	 * if registered in system and it's data should flow in postgres database
	 */
	private boolean monitored;

	/**
	 * moved into storage/history
	 */
	private boolean stored = false;

	@Indexed
	private String deviceName;

	@Indexed
	private String deviceHumanName;

	/*
	 * deviceId & sensorId are initialized when looking for sensor
	 */
	@Indexed
	private String deviceId;

	@Indexed
	private String sensorId;

	@Indexed
	private String provider;

	@Indexed
	private String name;
	private SensorType type;
	@Indexed
	private long timestampt;

	/*
	* actual sensor's value
	*/
	private Double value;

	public void updateValue(SensorValue value) {
		setDeviceName(value.getDeviceName());
		setProvider(value.getProvider());
		setName(value.getSensorName());

		setType(value.getType());
		setTimestampt(value.getTimestampt());
		setValue(value.getValue());
	}

	public void updateDeviceSensor(Device device, DeviceSensor sensor) {

		setDeviceId(device.getId().toString());
		setSensorId(sensor.getId().toString());

		setDeviceHumanName(device.getHumanName());
		setName(sensor.getName());
		setMonitored(sensor.isMonitored());
	}
}
