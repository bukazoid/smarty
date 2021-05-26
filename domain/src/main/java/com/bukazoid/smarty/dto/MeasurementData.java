package com.bukazoid.smarty.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MeasurementData {
	List<SensorValue> measures = new ArrayList<>();
	// property
	Object rawData;

	long timestampt = System.currentTimeMillis();

	public void add(SensorValue measure) {
		if (measure != null) {
			measures.add(measure);
		}
	}
}
