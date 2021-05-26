package com.bukazoid.smarty.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bukazoid.smarty.domain.store.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
