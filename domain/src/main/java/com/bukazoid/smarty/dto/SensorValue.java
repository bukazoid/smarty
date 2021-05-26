package com.bukazoid.smarty.dto;

import lombok.Data;

@Data
public class SensorValue {

	/**
	 * plant/tasmota/whatever
	 */
	private String provider;

	/**
	 * device id, usually it's MAC
	 */
	private String deviceName;

	/**
	 * measure name
	 * mostly it is copy of type, but it could be two temperature sensors at device, don't ask me
	 * 
	 * if null type will be used
	 */
	private String sensorName;

	private SensorType type;

	/**
	 * when data received
	 */
	private long timestampt = System.currentTimeMillis();

	private Double value;

}
