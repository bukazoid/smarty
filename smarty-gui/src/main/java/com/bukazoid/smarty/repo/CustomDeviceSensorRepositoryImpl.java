package com.bukazoid.smarty.repo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.domain.store.view.SensorProjection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomDeviceSensorRepositoryImpl implements CustomDeviceSensorRepository {

	private final EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<DeviceSensor> findByViewId(UUID viewId) {
		TypedQuery<SensorProjection> query = em.createQuery("select s from SensorProjection s WHERE s.view.id=:viewId", SensorProjection.class);
		query.setParameter("viewId", viewId);
		List<SensorProjection> projections = query.getResultList();
		List<DeviceSensor> sensors = projections.stream().map(SensorProjection::getSensor).collect(Collectors.toList());
		sensors.forEach(s -> log.info("id: {}", s.getId()));
		// sensors.forEach(s -> em.detach(s));
		return sensors;
	}
}
