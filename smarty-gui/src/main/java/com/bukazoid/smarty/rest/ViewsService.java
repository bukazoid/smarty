package com.bukazoid.smarty.rest;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.domain.store.view.SensorProjection;
import com.bukazoid.smarty.domain.store.view.View;
import com.bukazoid.smarty.repo.ProjectionRepository;
import com.bukazoid.smarty.repo.SensorRepository;
import com.bukazoid.smarty.repo.ViewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ViewsService {
	private final ProjectionRepository projectionRepo;
	private final SensorRepository sensorRepo;
	private final ViewRepository viewRepo;

	@Transactional(readOnly = false)
	@RequestMapping(value = "/api/views/attach/{viewId}/{sensorId}", method = RequestMethod.GET) // is it legal?
	public SensorProjection attach(@PathVariable("viewId") UUID viewId, @PathVariable("sensorId") UUID sensorId) {
		SensorProjection projection = new SensorProjection();
		DeviceSensor sensor = sensorRepo.findById(sensorId).orElse(null);
		View view = viewRepo.findById(viewId).orElse(null);
		projection.setSensor(sensor);
		projection.setView(view);

		projection.setName(sensor.getDevice().getHumanName() + "." + sensor.getName());
		projectionRepo.save(projection);

		return projection;
	}

	@Transactional(readOnly = false)
	@RequestMapping(value = "/api/views/detach/{viewId}/{sensorId}", method = RequestMethod.GET) // is it legal?
	public boolean detach(@PathVariable("viewId") UUID viewId, @PathVariable("sensorId") UUID sensorId) {
		SensorProjection projection = projectionRepo.findByViewIdAndSensorId(viewId, sensorId);
		if (projection != null) {
			log.info("to delete: {}, ", projection);
			projectionRepo.deleteById(projection.getId());
			return true;
		}

		log.info("projection is not found, vid: {}, sid: {}", viewId, sensorId);
		return false;

	}
}
