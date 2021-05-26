package com.bukazoid.smarty.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bukazoid.smarty.domain.store.SimpleUser;

@RepositoryRestResource(collectionResourceRel = "users", path = "susers")
public interface SimpleUserRepository extends JpaRepository<SimpleUser, UUID> {
	SimpleUser findByLogin(String name);
}
