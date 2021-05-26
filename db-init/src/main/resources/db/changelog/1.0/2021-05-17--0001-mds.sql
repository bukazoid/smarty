--liquibase formatted sql

--changeset bukazoid:2021-05-17--0005-mds
CREATE TABLE messaging_profile
(
   profile_id uuid,
   user_id uuid,
   name VARCHAR (32) NOT NULL,
   mds_name VARCHAR (32) NOT NULL,
   target VARCHAR(256) NOT NULL,
   FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
   PRIMARY KEY (profile_id)
);

--changeset bukazoid:2021-05-23--0003-sensor_projection_msg_profile
CREATE TABLE sensor_projection_msg_profile
(
   projection_id uuid,
   profile_id uuid,
   FOREIGN KEY (projection_id) REFERENCES sensor_projection (projection_id) ON DELETE CASCADE,
   FOREIGN KEY (profile_id) REFERENCES messaging_profile (profile_id) ON DELETE CASCADE,
   PRIMARY KEY (projection_id, profile_id)
);

--changeset bukazoid:2021-05-23--0007-view-profile
CREATE TABLE view_msg_profile
(
   view_id uuid,
   profile_id uuid,
   FOREIGN KEY (view_id) REFERENCES views (view_id) ON DELETE CASCADE,
   FOREIGN KEY (profile_id) REFERENCES messaging_profile (profile_id) ON DELETE CASCADE,
   PRIMARY KEY (view_id, profile_id)
);
