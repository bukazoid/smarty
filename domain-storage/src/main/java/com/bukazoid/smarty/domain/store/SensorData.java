package com.bukazoid.smarty.domain.store;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Table(name = "sensor_data")
@Entity
public class SensorData {
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "data_id")
	@Id
	UUID id;

	@ManyToOne(targetEntity = DeviceSensor.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sensor_id")
	DeviceSensor sensor;

	@Column(name = "data_time")
	LocalDateTime createTime;

	@Column(name = "value")
	double value;
}
