--liquibase formatted sql
--changeset bukazoid:2021-05-24--0001-projections-alarm
ALTER TABLE sensor_projection ADD COLUMN alarm_on BOOLEAN NOT NULL DEFAULT FALSE;
