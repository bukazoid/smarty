package com.bukazoid.smarty.services;

import java.util.List;
import java.util.Set;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.domain.store.MessagingProfile;
import com.bukazoid.smarty.domain.store.view.SensorProjection;
import com.bukazoid.smarty.dto.MeasurementData;
import com.bukazoid.smarty.dto.SensorValue;
import com.bukazoid.smarty.dto.SmartyQueries;
import com.bukazoid.smarty.dto.mds.MdsAlert;
import com.bukazoid.smarty.repositories.ProjectionRepository;
import com.bukazoid.smarty.repositories.SensorRepository;
import com.bukazoid.smarty.repositories.ViewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RaMqListener {
	private final ObjectMapper objectMapper;
	private final ViewRepository viewRepo;
	private final ProjectionRepository projectionRepo;
	private final SensorRepository sensorRepo;
	private final RabbitTemplate rabbit;

	/**
	 * idea was to not interact with data base here, only with redis, but it is too many things to do, so ..
	 * it will be rewritten
	 * @param message
	 * @throws JsonProcessingException
	 */
	@Transactional(readOnly = false)
	@RabbitListener(queues = SmartyQueries.QUEUE_RANGE_CHECKER)
	public void processMessage(String message) throws JsonProcessingException {
		try {
			MeasurementData data = objectMapper.readValue(message, MeasurementData.class);

			data.getMeasures().forEach(this::handleValue);

			log.info("RA: RECEIVED message: {}", data);

		} catch (Exception e) {
			log.error("can't read message", e);
		}
	}

	private void handleValue(SensorValue value) {
		DeviceSensor sensor = sensorRepo.findByNameAndDeviceNameAndDeviceProvider(value.getSensorName(), value.getDeviceName(), value.getProvider());
		if (sensor == null) {
			return;
		}

		List<SensorProjection> projections = projectionRepo.findBySensorId(sensor.getId());
		projections.forEach(p -> validateValueWrap(value.getValue(), p));
	}

	private void validateValueWrap(Double value, SensorProjection projection) {
		try {
			validateValue(value, projection);
		} catch (Exception e) {
			log.error("validateValue", e);
		}
	}

	private void validateValue(Double value, SensorProjection projection) throws AmqpException, JsonProcessingException {
		if (projection.getMinValue() != null) {
			if (projection.getMinValue() > value && !projection.isAlarmOn()) {

				processMessaging(projection, String.format("Value(%s) of sensor '%s' is less then allowed minimum '%s'", value, projection.getName(),
						projection.getMinValue()));

				projection.setAlarmOn(true);
				projectionRepo.save(projection);
				return;
			}
		}

		if (projection.getMaxValue() != null) {
			if (projection.getMaxValue() < value && !projection.isAlarmOn()) {
				// alarm
				processMessaging(projection, String.format("Value(%s) of sensor '%s' is more then allowed maximum '%s'", value, projection.getName(),
						projection.getMaxValue()));

				projection.setAlarmOn(true);
				projectionRepo.save(projection);
				return;
			}
		}

		if (projection.isAlarmOn()) {
			projection.setAlarmOn(false);
			projectionRepo.save(projection);

			processMessaging(projection, String.format("Value(%s) of sensor '%s' is OK now", value, projection.getName()));
		}
	}

	private void processMessaging(SensorProjection projection, String text) throws AmqpException, JsonProcessingException {
		log.info("processMessaging for {}", projection.getName());
		Set<MessagingProfile> profiles = projection.getProfiles();

		log.error("found {} projection's profiles", profiles.size());
		if (profiles.isEmpty()) {
			profiles = projection.getView().getProfiles();
			log.error("found {} view's profiles", profiles.size());
		}
		for (MessagingProfile profile : profiles) {

			// alarm
			MdsAlert message = new MdsAlert();
			message.setDeliverySystem(profile.getMdsName());
			message.setTarget(profile.getTarget());
			message.setMessage(text);
			rabbit.convertAndSend(SmartyQueries.MDS_ALERT, objectMapper.writeValueAsString(message));
		}
	}
}
