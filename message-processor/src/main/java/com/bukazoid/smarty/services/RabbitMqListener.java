package com.bukazoid.smarty.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.redis.CurrentValue;
import com.bukazoid.smarty.domain.store.Device;
import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.dto.MeasurementData;
import com.bukazoid.smarty.dto.SensorValue;
import com.bukazoid.smarty.dto.SmartyQueries;
import com.bukazoid.smarty.repositories.DeviceRepository;
import com.bukazoid.smarty.repositories.DeviceSensorRepository;
import com.bukazoid.smarty.repositories.ValuesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMqListener {
	private final ObjectMapper objectMapper;
	private final ValuesRepository redisRepo;

	@RabbitListener(queues = SmartyQueries.QUEUE_MESSAGE_PROCESSOR)
	public void processMessage(String message) throws JsonProcessingException {
		try {
			MeasurementData data = objectMapper.readValue(message, MeasurementData.class);

			log.info("RECEIVED message: {}", data);

			data.getMeasures().forEach(value -> {
				try {
					saveValue(value);
				} catch (Throwable e) {
					log.error("redis.save ", e);
				}
			});

		} catch (Exception e) {
			log.error("can't read message", e);
		}
	}

	private CurrentValue saveValue(SensorValue value) {
		CurrentValue current = redisRepo.findByProviderAndDeviceNameAndName(value.getProvider(), value.getDeviceName(), value.getSensorName());
		if (current == null) {
			current = new CurrentValue();
		}

		current.updateValue(value);

		redisRepo.save(current);
		return current;
	}

}