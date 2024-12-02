CREATE TABLE "user"
(
    id         BIGSERIAL NOT NULL,
    name       VARCHAR(500) NOT NULL,
    password   VARCHAR(256) NOT NULL,
    login      VARCHAR(256) NOT NULL,
    email      VARCHAR(256) NOT NULL,
    enabled    BOOL,
    company_id BIGINT,
    PRIMARY KEY (id)
);