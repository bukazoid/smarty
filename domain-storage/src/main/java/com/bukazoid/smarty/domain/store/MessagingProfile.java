package com.bukazoid.smarty.domain.store;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.Data;

/**
 * profile about hot to send message
 * @author frozen
 *
 */

@TypeDefs({ @TypeDef(name = "json", typeClass = JsonType.class) })
@Data
@Table(name = "messaging_profile")
@Entity
public class MessagingProfile {

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "profile_id")
	@Id
	UUID id;

	@Column(name = "name")
	String name;

	/**
	 * message delivery system name	
	 */
	@Column(name = "mds_name")
	String mdsName;

	@Column(name = "target")
	String target;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "user_id")
	// User user;

	// @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profiles")
	// // @JoinTable(name = "sensor_projection_msg_profile", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name =
	// // "projection_id"))
	// Set<SensorProjection> projections;

	// @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profiles")
	// // @JoinTable(name = "view_msg_profile", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "view_id"))
	// Set<View> views;
}
