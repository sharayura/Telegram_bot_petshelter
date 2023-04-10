-- liquibase formatted sql

-- changeset yuri:1
CREATE TABLE if not exists users
(
    chat_id BIGINT NOT NULL PRIMARY KEY,
    name    TEXT   NOT NULL,
    stage   INT    NOT NULL,
    contact TEXT
);