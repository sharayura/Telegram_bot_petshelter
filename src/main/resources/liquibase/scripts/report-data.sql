-- liquibase formatted sql

-- changeset yuri:1
CREATE TABLE if not exists reports
(
    date_chat_id VARCHAR PRIMARY KEY,
    chat_id      BIGINT NOT NULL,
    date         DATE   NOT NULL,
    photo        oid,
    ration       TEXT,
    health       TEXT,
    habits       TEXT

);