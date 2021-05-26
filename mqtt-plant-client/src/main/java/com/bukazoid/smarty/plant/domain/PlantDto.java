package com.bukazoid.smarty.plant.domain;

import lombok.Data;

@Data
public class PlantDto {
	Double temperature;// optional
	Double pressure;// optional
	Double humidity;// optional
	Double lux;
	Double batt;
	Double soil;
	Double soilPercent;
	Double salt;
	String mac;
	String ip;
	/**
	 * number of boots
	 */
	long bootcount;
	/**
	 * version
	 */
	String firmware;
	/**
	 * active phase in ticks
	 */
	long ticks;
	/**
	 * active phase time
	 */
	long worktime;
	/**
	 * wifi connection timeout
	 */
	long wifiCount;
}
