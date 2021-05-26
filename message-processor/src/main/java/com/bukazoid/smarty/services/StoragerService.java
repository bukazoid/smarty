package com.bukazoid.smarty.services;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.redis.CurrentValue;
import com.bukazoid.smarty.domain.store.SensorData;
import com.bukazoid.smarty.repositories.SensorDataRepository;
import com.bukazoid.smarty.repositories.ValuesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoragerService {
	private final ValuesRepository redisRepo;
	private final SensorDataRepository dataRepo;
	private final ProcessorTools tools;

	@Scheduled(cron = "0 0/5 * * * ?") // XXX: 0/15
	public void dumpData() {
		log.info("time to dump data, count: {}", redisRepo.count());

		redisRepo.findAll().forEach(this::updateDeviceSensor);

		redisRepo.findAll().forEach(this::saveInDb);
	}

	private void updateDeviceSensor(CurrentValue current) {
		if (current.getSensorId() != null) {
			return;
		}

		log.info("create/find device/sensor");
		tools.initSensor(current);
		redisRepo.save(current);
		log.info("device/s: {}/{}", current.getDeviceId(), current.getSensorId());
	}

	private void saveInDb(CurrentValue sva) {
		if (sva == null) {
			log.warn("empty value, what a shame.. Did you change domain package, again?");
			return;
		}
		if (!sva.isMonitored()) {
			// not monitored
			return;
		}
		if (sva.isStored()) {
			log.info("sensor's data({}) already stored", sva.getId());
			return;
		}

		Optional<SensorData> data = tools.genSensorData(sva);

		data.ifPresent(dat -> {
			dataRepo.save(dat);
			// set stored flag to prevent it's next storage
			sva.setStored(true);
			redisRepo.save(sva);
		});
	}

}
