package com.bukazoid.smarty.domain.store;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * no password here	
 * @author frozen
 *
 */
@Data
@Table(name = "users")
@Entity
public class SimpleUser {
	@Column(name = "user_id")
	@Id
	UUID id;

	@Column(name = "user_login")
	String login;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type")
	UserType type;
}
