package com.bukazoid.smarty.mds.repositories;

import java.util.UUID;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

import com.bukazoid.smarty.domain.redis.CurrentValue;

public interface ValuesRepository extends KeyValueRepository<CurrentValue, UUID> {
	CurrentValue findByProviderIdAndDeviceNameAndName(String providerId, String deviceId, String name);
}
