package com.bukazoid.smarty.domain.store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Table(name = "devices")
@ToString(exclude = {"sensors"})
@Entity
@NoArgsConstructor
public class Device {
	@Column(name = "device_id")
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	UUID id;

	@Column(name = "provider_id")
	String provider;

	@Column(name = "device_name")
	String name;

	@Column(name = "device_human_name")
	String humanName;

	@Column(name = "device_location")
	String location;

	@Column(name = "create_time")
	LocalDateTime createTime = LocalDateTime.now();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "device_id")
	private List<DeviceSensor> sensors = new ArrayList<>();
}
