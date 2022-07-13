-- liquibase formatted sql

-- changeset jongsun.park:jongsun.park-220629-1
ALTER TABLE proof_of_deliveries ADD trip_stops_id BIGINT NULL COMMENT '배송지 ID'  AFTER `id`;

