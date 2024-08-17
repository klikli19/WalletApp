-- liquibase formatted sql
-- changeset klikli:1

CREATE TABLE wallet
(
    id      UUID NOT NULL UNIQUE ,
    balance BIGINT,
    PRIMARY KEY (id)
    );