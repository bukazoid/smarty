package com.bukazoid.smarty.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bukazoid.smarty.dto.mds.MdsCommon;
import com.bukazoid.smarty.dto.mds.MdsRegister;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MdsService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper mapper;

	@Transactional(readOnly = true)
	@RequestMapping(value = "/api/mds/", method = RequestMethod.GET)
	public List<String> getMdsList() {
		if (!redisTemplate.hasKey(MdsCommon.MDS_KEY)) {
			return Collections.emptyList();
		}

		List<MdsRegister> systems = (List<MdsRegister>) redisTemplate.opsForValue().get(MdsCommon.MDS_KEY);

		return systems.stream().map(MdsRegister::getName).collect(Collectors.toList());
	}
		
}
