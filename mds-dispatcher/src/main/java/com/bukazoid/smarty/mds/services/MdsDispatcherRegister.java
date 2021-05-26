package com.bukazoid.smarty.mds.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.dto.SmartyQueries;
import com.bukazoid.smarty.dto.mds.MdsCommon;
import com.bukazoid.smarty.dto.mds.MdsRegister;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MdsDispatcherRegister {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper mapper;

	@RabbitListener(queues = SmartyQueries.MDS_REGISTER)
	public void processMessage(String message) throws JsonProcessingException {
		try {
			log.info("MDS_REGISTER message: {}", message);
			MdsRegister mds2register = mapper.readValue(message, MdsRegister.class);

			if (!redisTemplate.hasKey(MdsCommon.MDS_KEY)) {
				// create single element
				List<MdsRegister> systems = Arrays.asList(mds2register);
				redisTemplate.opsForValue().set(MdsCommon.MDS_KEY, systems);
			} else {
				// add
				List<MdsRegister> systems = (List<MdsRegister>) redisTemplate.opsForValue().get(MdsCommon.MDS_KEY);
				systems = new ArrayList<>(systems);
				// check if already exist
				Optional<MdsRegister> register = systems.stream().filter(sys -> sys.getName().equals(mds2register.getName())).findFirst();
				if (register.isEmpty()) {
					systems.add(mds2register);
					redisTemplate.opsForValue().set(MdsCommon.MDS_KEY, systems);
				} else {
					log.info("already registered");
				}
			}

		} catch (Exception e) {
			log.error("can't read message", e);
		}
	}
}