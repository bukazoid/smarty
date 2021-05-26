package com.bukazoid.smarty.service;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.store.MessagingProfile;
import com.bukazoid.smarty.domain.store.User;
import com.bukazoid.smarty.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RepositoryEventHandler(MessagingProfile.class)
@RequiredArgsConstructor
public class MessagingProfileHandler {
	private final UserRepository userRepo;

	// @HandleBeforeCreate
	// public void handleProfileCreate(MessagingProfile profile) {
	// // log.info("set user before create");
	// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// User user = userRepo.findByLogin(auth.getName());
	// profile.setUser(user);
	// }
	@HandleAfterCreate
	public void handleProfileCreate(MessagingProfile profile) {
		// log.info("set user before create");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByLogin(auth.getName());
		user.getProfiles().add(profile);
		userRepo.save(user);
	}
}