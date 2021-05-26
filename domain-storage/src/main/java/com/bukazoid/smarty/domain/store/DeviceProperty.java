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

import lombok.Data;

@Data
@Table(name = "device_properties")
@Entity
public class DeviceProperty {
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "property_id")
	@Id
	UUID id;

	@ManyToOne(targetEntity = Device.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "device_id")
	Device device;

	@Column(name = "property_name")
	String name;

	@Column(name = "property_value")
	String value;
}
