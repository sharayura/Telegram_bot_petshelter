-- liquibase formatted sql

-- changeset yuri:1
CREATE TABLE if not exists users
(
    chat_id      BIGINT NOT NULL PRIMARY KEY,
    name         TEXT   NOT NULL,
    contact      TEXT,
    days_trial   INTEGER,
    dog_name     TEXT,
    fails_in_row INT

);