package com.bukazoid.smarty.service;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.store.User;
import com.bukazoid.smarty.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * based on second answer:
 * https://stackoverflow.com/questions/30250851/spring-data-rest-user-repository-bccrypt-password
 */
@Slf4j
@Service
@RepositoryEventHandler(User.class)
@RequiredArgsConstructor
public class UserEventHandler {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	@HandleBeforeCreate
	public void handleUserCreate(User user) {
		log.info("before create");
		if (user.getPassword() != null && user.getPassword().toLowerCase().startsWith("$2a")) {
			// keep it
			return;
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}

	@HandleBeforeSave
	public void handleUserUpdate(User user) {
		log.info("before update, password: {}", user.getPassword());
		if (user.getPassword() == null || user.getPassword().equals("")) {
			// keeps the last password
			User storedUser = userRepository.getOne(user.getId());
			user.setPassword(storedUser.getPassword());
		} else if (user.getPassword().toLowerCase().startsWith("$2a")) {
			// keep password
		} else {
			// password change request
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
	}

}