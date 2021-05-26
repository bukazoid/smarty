package com.bukazoid.smarty.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bukazoid.smarty.domain.redis.CurrentValue;
import com.bukazoid.smarty.domain.store.Device;
import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.domain.store.User;
import com.bukazoid.smarty.dto.SensorValue;
import com.bukazoid.smarty.repo.DeviceRepository;
import com.bukazoid.smarty.repo.SensorRepository;
import com.bukazoid.smarty.repo.UserRepository;
import com.bukazoid.smarty.repo.redis.ValuesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * clone of ProcessorTool from message-processor project.
 * it looks we need to call message processor instead of database for registration purposes
 * @author frozen
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessorToolsImpl implements PlatformTools {
	private final DeviceRepository deviceRepository;
	private final SensorRepository sensorRepository;
	private final ValuesRepository valuesRepo;
	private final UserRepository userRepo;

	/**
	 * XXX: optimize, it can be done in one request
	 * @param value
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public DeviceSensor getSensor(SensorValue value) {
		Device device = deviceRepository.findByNameAndProvider(value.getDeviceName(), value.getProvider());
		if (device == null) {
			return null;
		}

		DeviceSensor sensor = sensorRepository.findByNameAndDeviceId(value.getSensorName(), device.getId());

		return sensor;
	}

	@Override
	@Transactional(readOnly = true)
	public DeviceSensor getSensor(CurrentValue value) {
		return sensorRepository.findById(UUID.fromString(value.getId())).orElse(null);
	}

	@Transactional(readOnly = true)
	private User getCurrentUser() {
		// XXX:
		return userRepo.findByLogin("admin");
	}

	@Override
	@Transactional
	public boolean unregister(String id) {
		// update current value to be registered
		// it has to be done even if sensordo not exists
		Optional<CurrentValue> value = valuesRepo.findById(id);

		value.ifPresent(val -> {
			val.setMonitored(false);
			valuesRepo.save(val);
			sensorRepository.deleteById(UUID.fromString(id));
		});

		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean register(String id) {
		Optional<CurrentValue> value = valuesRepo.findById(id);
		value.ifPresent(val -> {
			val.setMonitored(true);
			valuesRepo.save(val);
		});

		return true;
	}
}
