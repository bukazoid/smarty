package com.bukazoid.smarty.domain.store;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.bukazoid.smarty.dto.SensorType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "device" })
@Data
@ToString(exclude = {"device"})
@Table(name = "device_sensors")
@Entity
public class DeviceSensor {
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "sensor_id")
	@Id
	UUID id;

	@ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	Device device;

	@Column(name = "sensor_name")
	String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "sensor_type")
	SensorType type;

	@Column(name = "create_time")
	LocalDateTime createTime = LocalDateTime.now();

	@Column(name = "monitored")
	boolean monitored = false;
}
