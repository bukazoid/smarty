--liquibase formatted sql
--changeset bukazoid:2021-05-09--0001-views
CREATE TABLE views
(
   view_id uuid,
   name VARCHAR (32) NOT NULL,
   PRIMARY KEY (view_id)
);
CREATE TABLE sensor_projection
(
   projection_id uuid,
   name VARCHAR (32) NOT NULL,
   min_value REAL,
   max_value REAL,
   view_id uuid,
   sensor_id uuid,
   PRIMARY KEY (projection_id),
   FOREIGN KEY (view_id) REFERENCES views (view_id) ON DELETE CASCADE,
   FOREIGN KEY (sensor_id) REFERENCES device_sensors (sensor_id) ON DELETE CASCADE
);

--changeset bukazoid:2021-05-17--0001-view-user
CREATE TABLE view_user
(
   view_id uuid,
   user_id uuid,
   PRIMARY KEY (view_id, user_id)
);

--changeset bukazoid:2021-05-17--0002-view-user
DROP TABLE view_user;

--changeset bukazoid:2021-05-17--0003-view-user
CREATE TABLE view_user
(
   view_id uuid,
   user_id uuid,
   FOREIGN KEY (view_id) REFERENCES views (view_id) ON DELETE CASCADE,
   FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
   PRIMARY KEY (view_id, user_id)
);


