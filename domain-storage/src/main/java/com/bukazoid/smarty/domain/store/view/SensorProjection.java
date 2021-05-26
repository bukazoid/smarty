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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.bukazoid.smarty.domain.store.DeviceSensor;
import com.bukazoid.smarty.domain.store.MessagingProfile;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = { "view", "sensor" })
@Table(name = "sensor_projection")
@Entity
public class SensorProjection {
	@Column(name = "projection_id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Id
	UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "view_id")
	View view;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_id")
	DeviceSensor sensor;

	/**
	 * to have hateoas and avoid unnecessary requests
	 */
	@Indexed
	@Column(name = "sensor_id", insertable = false, updatable = false)
	UUID sensorId;

	/**
	 * sensor's name for the view, copied from sensor but can be updated
	 */
	String name;

	/**
	 * alarm if sensor's value less then this one
	 */
	@Column(name = "min_value")
	Double minValue;

	/**
	 * alarm if sensor's value more then this one
	 */
	@Column(name = "max_value")
	Double maxValue;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sensor_projection_msg_profile", joinColumns = @JoinColumn(name = "projection_id"), inverseJoinColumns = @JoinColumn(name = "profile_id"))
	Set<MessagingProfile> profiles;
	
	@Column(name = "alarm_on")
	boolean alarmOn;
}
