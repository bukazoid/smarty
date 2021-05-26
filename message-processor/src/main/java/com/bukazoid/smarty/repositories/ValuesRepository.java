package com.bukazoid.smarty.repositories;

import java.util.UUID;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bukazoid.smarty.domain.redis.CurrentValue;


@RepositoryRestResource(collectionResourceRel = "values", path = "values")
public interface ValuesRepository extends KeyValueRepository<CurrentValue, UUID> {
	CurrentValue findByProviderAndDeviceNameAndName(String providerId, String deviceId, String name);
}
