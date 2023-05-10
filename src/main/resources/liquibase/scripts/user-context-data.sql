-- liquibase formatted sql

-- changeset yuri:1
CREATE TABLE if not exists user_contexts
(
    chat_id      BIGINT NOT NULL PRIMARY KEY,
    last_command TEXT,
    stage        INTEGER

);