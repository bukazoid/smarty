package com.bukazoid.smarty.dto;

public enum SensorType {
	/**
	 * air(?) temperature
	 * -40..+40
	 */
	TEMPERATURE,
	/**
	 * huminidy, actually air huminidy
	 * 0..100
	 */
	HUMINIDY,
	/**
	 * Soil moisture
	 * 0..100
	 */
	SOIL,
	/**
	 * salt for plant, 200-350 is ok, maybe individual
	 */
	SALT,

	LUX,
	/**
	 * air pressue
	 * ??
	 */
	PRESSURE,
	/**
	 * same as positive long, like number os seconds or hours or peaople
	 * 0..MAX_LONG
	 */
	COUNT,
	/**
	 * some unknown or user type
	 * any float
	 */
	FLOAT,
	/**
	 * true(1) or false(0)
	 */
	BOOLEAN,
}
