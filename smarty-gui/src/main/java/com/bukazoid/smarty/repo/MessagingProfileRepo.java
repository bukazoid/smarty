package com.bukazoid.smarty.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bukazoid.smarty.domain.store.MessagingProfile;

@RepositoryRestResource(collectionResourceRel = "msgProfiles", path = "msgProfiles")
public interface MessagingProfileRepo extends JpaRepository<MessagingProfile, UUID>, CustomMessagingProfileRepository {

}
