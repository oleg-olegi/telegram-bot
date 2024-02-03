-- liquibase formatted sql

--changeSet Oleg:1
CREATE TABLE tasks
(
    id                SERIAL PRIMARY KEY,
    chat_id           INTEGER    NOT NULL,
    notification_task VARCHAR(255),
    scheduled_time    TIMESTAMP NOT NULL
);
