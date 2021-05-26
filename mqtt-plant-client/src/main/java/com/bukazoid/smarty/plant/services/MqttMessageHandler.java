package com.bukazoid.smarty.plant.services;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import com.bukazoid.smarty.dto.MeasurementData;
import com.bukazoid.smarty.dto.SensorType;
import com.bukazoid.smarty.dto.SensorValue;
import com.bukazoid.smarty.plant.domain.PlantDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tocrhz.mqtt.annotation.MqttSubscribe;
import com.github.tocrhz.mqtt.annotation.NamedValue;
import com.github.tocrhz.mqtt.annotation.Payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MqttMessageHandler {
	private final String PROVIDER = "PLANT_MQTT";// grab from spring config
	private final MessageSender sender;
	private final ObjectMapper objectMapper;

	@MqttSubscribe("plant/{id}")
	public void subPlant(@NamedValue("id") String id, String topic, MqttMessage message, @Payload String payload) {
		try {
			PlantDto dto = objectMapper.readValue(message.getPayload(), PlantDto.class);
			MeasurementData listDto = new MeasurementData();
			listDto.add(genMeasure(id, SensorType.PRESSURE, dto.getPressure()));
			listDto.add(genMeasure(id, SensorType.TEMPERATURE, dto.getTemperature()));
			listDto.add(genMeasure(id, SensorType.HUMINIDY, dto.getHumidity()));

			listDto.add(genMeasure(id, SensorType.COUNT, "Boot Count", dto.getBootcount()));
			listDto.add(genMeasure(id, SensorType.COUNT, "Process Time", dto.getWorktime()));
			listDto.add(genMeasure(id, SensorType.COUNT, "WiFi Connect Time", dto.getWifiCount()));

			listDto.add(genMeasure(id, SensorType.SOIL, dto.getSoilPercent()));
			listDto.add(genMeasure(id, SensorType.SALT, dto.getSalt()));
			listDto.add(genMeasure(id, SensorType.LUX, dto.getLux()));
			listDto.add(genMeasure(id, SensorType.FLOAT, "Battery Voltage", adaptBatteryVoltage(dto.getBatt())));
			// listDto.add(genMeasure(id, SensorType.IP, dto.getIp()));// to je property

			listDto.setRawData(dto);
			sender.sendMessage(listDto);
		} catch (Throwable th) {
			log.error("can't handle PlantDto", th);
		}
	}

	private Double adaptBatteryVoltage(Double voltage) {
		if (voltage == null) {
			return null;
		}

		if (voltage > 1000) {
			// old format
			return voltage / 1000f;
		}
		return voltage;
	}

	private SensorValue genMeasure(String id, SensorType type, String name, Long value) {
		return genMeasure(id, type, name, value.doubleValue());
	}

	private SensorValue genMeasure(String id, SensorType type, String name, Double value) {
		if (value == null) {
			return null;
		}

		SensorValue val = new SensorValue();

		val.setValue(value);

		val.setDeviceName(id);
		val.setProvider(PROVIDER);
		val.setSensorName(name);
		val.setType(type);

		return val;
	}

	private SensorValue genMeasure(String id, SensorType type, Double value) {
		return genMeasure(id, type, type.name(), value);
	}

}