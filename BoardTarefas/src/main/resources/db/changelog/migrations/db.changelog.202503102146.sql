-- liquibase formatted sql
-- changeset andre:202503102146
-- coment: boards table created

CREATE TABLE BOARDS(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- rollback DROP TABLE BOARDS