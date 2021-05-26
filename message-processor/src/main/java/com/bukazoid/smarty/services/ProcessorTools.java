package com.bukazoid.smarty.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.redis.CurrentValue;
import com.bukazoid.smarty.domain.store.Device;
import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.domain.store.SensorData;
import com.bukazoid.smarty.repositories.DeviceRepository;
import com.bukazoid.smarty.repositories.DeviceSensorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessorTools {
	private final DeviceRepository deviceRepo;
	private final DeviceSensorRepository sensorRepository;

	public void initSensor(CurrentValue current) {

		// find device
		Device device = deviceRepo.findByNameAndProvider(current.getDeviceName(), current.getProvider());
		DeviceSensor sensor = null;
		if (device == null) {
			log.info("create device/sensor");
			// create device and sensor
			device = genDeviceFromValue(current);
			deviceRepo.save(device);

			log.info("deviceId: {}", device.getId());

			sensor = genSensorFromValue(device, current);
			sensorRepository.save(sensor);
			log.info("sensorId: {}", sensor.getId());
		} else {
			log.info("create sensor");
			sensor = sensorRepository.findByNameAndDeviceId(current.getName(), device.getId());
			if (sensor == null) {
				sensor = genSensorFromValue(device, current);
				sensorRepository.save(sensor);
				log.info("sensorId: {}", sensor.getId());
			}
		}

		log.info("update");
		current.updateDeviceSensor(device, sensor);
	}

	public Optional<SensorData> genSensorData(CurrentValue sva) {
		Optional<DeviceSensor> sensor = sensorRepository.findById(UUID.fromString(sva.getId()));

		return sensor.map(s -> {
			SensorData sData = new SensorData();
			// sData.setId(UUID.randomUUID());
			sData.setValue(sva.getValue());
			sData.setSensor(s);
			LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(sva.getTimestampt()), ZoneOffset.UTC);
			sData.setCreateTime(dateTime);
			return sData;
		});
	}

	public Device genDeviceFromValue(CurrentValue value) {
		Device dev = new Device();
		// dev.setId(UUID.randomUUID());
		dev.setProvider(value.getProvider());
		dev.setName(value.getDeviceName());
		dev.setHumanName(value.getDeviceName());
		dev.setLocation("no room");
		return dev;
	}

	public DeviceSensor genSensorFromValue(Device device, CurrentValue value) {
		DeviceSensor sensor = new DeviceSensor();
		// sensor.setId(UUID.randomUUID());
		sensor.setDevice(device);
		sensor.setName(value.getName());
		sensor.setType(value.getType());
		return sensor;
	}

}
