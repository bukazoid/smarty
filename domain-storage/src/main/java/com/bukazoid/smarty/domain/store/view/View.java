package com.bukazoid.smarty.domain.store.view;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.bukazoid.smarty.domain.store.MessagingProfile;
import com.bukazoid.smarty.domain.store.SimpleUser;

import lombok.Data;

/**
 * view needed to represent set of sensors with rules how to react on them
 * @author frozen
 *
 */
@Data
@Table(name = "views")
@Entity
public class View {
	@Column(name = "view_id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Id
	UUID id;

	String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "view_user", joinColumns = @JoinColumn(name = "view_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	Set<SimpleUser> users;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "view_msg_profile", joinColumns = @JoinColumn(name = "view_id"), inverseJoinColumns = @JoinColumn(name = "profile_id"))
	Set<MessagingProfile> profiles;
}