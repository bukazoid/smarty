--liquibase formatted sql
--changeset bukazoid:2021-05-07--0001-base
CREATE TABLE devices
(
   device_id uuid,
   provider_id VARCHAR (32) NOT NULL,
   device_name VARCHAR (32) NOT NULL,
   -- usually mac address
   device_human_name VARCHAR (32) NOT NULL,
   device_location VARCHAR (128) NOT NULL,
   create_time timestamp NOT NULL,
   PRIMARY KEY (device_id),
   UNIQUE
   (
      provider_id,
      device_name
   )
);
CREATE TABLE device_properties
(
   property_id uuid,
   device_id uuid NOT NULL,
   property_name varchar (32) NOT NULL,
   property_value varchar (32),
   -- not sure if null ok
   PRIMARY KEY (property_id),
   FOREIGN KEY (device_id) REFERENCES devices (device_id) ON DELETE CASCADE,
   UNIQUE
   (
      device_id,
      property_name
   )
);
CREATE TABLE device_sensors
(
   sensor_id uuid,
   device_id uuid NOT NULL,
   sensor_name VARCHAR (32) NOT NULL,
   sensor_type VARCHAR (16) NOT NULL,
   create_time timestamp NOT NULL,
   monitored BOOLEAN NOT NULL DEFAULT FALSE,
   PRIMARY KEY (sensor_id),
   FOREIGN KEY (device_id) REFERENCES devices (device_id) ON DELETE CASCADE
);
CREATE TABLE sensor_data
(
   data_id uuid,
   sensor_id uuid NOT NULL,
   data_time timestamp NOT NULL,
   value real NOT NULL,
   PRIMARY KEY (data_id),
   FOREIGN KEY (sensor_id) REFERENCES device_sensors (sensor_id) ON DELETE CASCADE
);
CREATE INDEX idx_data_time ON sensor_data (data_time);

