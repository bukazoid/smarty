package com.bukazoid.smarty.domain.store;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class User {
	@Column(name = "user_id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Id
	UUID id;

	@Column(name = "user_login")
	String login;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type")
	UserType type;

	@Column(name = "user_password")
	String password;

	@Column(name = "create_time")
	LocalDateTime createTime = LocalDateTime.now();
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	Set<MessagingProfile> profiles = new HashSet<>();
}
