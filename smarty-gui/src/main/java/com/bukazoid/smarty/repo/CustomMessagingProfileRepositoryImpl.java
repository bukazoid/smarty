package com.bukazoid.smarty.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.bukazoid.smarty.domain.store.MessagingProfile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@RequiredArgsConstructor
public class CustomMessagingProfileRepositoryImpl implements CustomMessagingProfileRepository {

	private final EntityManager em;

	@Autowired
	@Lazy
	private MessagingProfileRepo baseRepository; /* Optional - if you need it */

	private final UserRepository userRepo;

	@Transactional(readOnly = true)
	@Override
	public List<MessagingProfile> findByCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return new ArrayList<>(userRepo.findByLogin(auth.getName()).getProfiles());
	}

}
