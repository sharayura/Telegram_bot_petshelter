-- liquibase formatted sql

-- changeset yuri:1
CREATE TABLE if not exists volunteers
(
    chat_id BIGINT NOT NULL PRIMARY KEY,
    name    TEXT   NOT NULL,
    contact TEXT
);
-- changeset yuri:2
insert into volunteers (chat_id, name) values (1669810768, 'Yuri');