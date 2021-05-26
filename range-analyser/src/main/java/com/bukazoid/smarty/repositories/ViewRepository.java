package com.bukazoid.smarty.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bukazoid.smarty.domain.store.view.View;

public interface ViewRepository extends JpaRepository<View, UUID> {
	List<View> findByUsersLogin(String login);
}