package com.bukazoid.smarty.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bukazoid.smarty.domain.redis.CurrentValue;
import com.bukazoid.smarty.domain.store.Device;
import com.bukazoid.smarty.domain.store.view.View;
import com.bukazoid.smarty.repo.DeviceRepository;
import com.bukazoid.smarty.repo.ViewRepository;
import com.bukazoid.smarty.repo.redis.ValuesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DeviceService {

	private final DeviceRepository deviceRepository;
	private final ValuesRepository valueRepository;
	private final ViewRepository viewRepository;

	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/views/userViews", method = RequestMethod.GET)
	public List<View> getViews() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		return viewRepository.findByUsersLogin(login);
	}

	@Deprecated
	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/device/{deviceId}", method = RequestMethod.GET)
	public Device read(@PathVariable("deviceId") String deviceId) {
		return deviceRepository.findById(UUID.fromString(deviceId)).orElse(null);
	}

	@Deprecated
	@Transactional(readOnly = false)
	@RequestMapping(value = "/api/device/{deviceId}", method = RequestMethod.PUT)
	public Object put(@PathVariable("deviceId") String deviceId, @RequestBody Device body) {
		if (deviceId == null || !deviceId.equalsIgnoreCase(body.getId().toString())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST);
		}

		deviceRepository.save(body);

		List<CurrentValue> iterator = valueRepository.findByDeviceId(deviceId);

		iterator.forEach(scv -> {
			scv.setDeviceName(body.getHumanName());
			valueRepository.save(scv);
		});

		return body;
	}

}
