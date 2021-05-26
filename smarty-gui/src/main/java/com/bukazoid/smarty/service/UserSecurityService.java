package com.bukazoid.smarty.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bukazoid.smarty.domain.store.User;
import com.bukazoid.smarty.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ROLE_USER = "ROLE_USER";

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		List<GrantedAuthority> authorities = getAuthorities(user);
		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
	}

	// public and static for test
	public static List<GrantedAuthority> getAuthorities(User user) {
		switch (user.getType()) {
			case ADMIN:
				return Arrays.asList(new SimpleGrantedAuthority(ROLE_ADMIN));
			case USER:
				return Arrays.asList(new SimpleGrantedAuthority(ROLE_USER));
			default:
				throw new RuntimeException("unsupported user type: " + user.getType());
		}
	}
}
