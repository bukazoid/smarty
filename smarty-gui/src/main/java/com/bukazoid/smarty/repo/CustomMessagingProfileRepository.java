package com.bukazoid.smarty.repo;

import java.util.List;

import com.bukazoid.smarty.domain.store.MessagingProfile;

public interface CustomMessagingProfileRepository {
	List<MessagingProfile> findByCurrentUser();

}
