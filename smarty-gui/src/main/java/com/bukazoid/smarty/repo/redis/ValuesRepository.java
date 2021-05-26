package com.bukazoid.smarty.repo.redis;

import java.util.List;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bukazoid.smarty.domain.redis.CurrentValue;

@RepositoryRestResource(collectionResourceRel = "values", path = "values")
public interface ValuesRepository extends KeyValueRepository<CurrentValue, String> {
	List<CurrentValue> findByDeviceId(String deviceId);

	List<CurrentValue> findByIdIn(List<String> ids);
}
