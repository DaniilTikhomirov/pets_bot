-- liquibase formatted sql

-- changeset danil:v0-1-1
CREATE TABLE telegram_users
(
    id              bigserial PRIMARY KEY,
    telegram_id     bigint NOT NULL,
    contact         varchar(90),
    warning_counter int DEFAULT (0),
    streak_counter  int DEFAULT (0)
);

-- changeset danil:v0-1-2
CREATE TABLE volunteers
(
    id          bigserial PRIMARY KEY,
    telegram_id bigint NOT NULL,
    contact     varchar(90),
    name        TEXT
);

-- changeset danil:v0-1-3
ALTER TABLE telegram_users
    ADD COLUMN volunteer_id bigint;
ALTER TABLE telegram_users
    ADD FOREIGN KEY (volunteer_id) REFERENCES volunteers (id);

-- changeset danil:v0-1-4
CREATE TABLE schedules
(
    id          bigserial PRIMARY KEY,
    shelters_id bigint,
    monday      varchar(90),
    tuesday     varchar(90),
    wednesday   varchar(90),
    thursday    varchar(90),
    friday      varchar(90),
    saturday    varchar(90),
    sunday      varchar(90)
);

-- changeset danil:v0-1-5
CREATE TABLE shelter_info
(
    id                        serial PRIMARY KEY,
    name                      TEXT   NOT NULL,
    description               TEXT   NOT NULL,
    schedules_id              bigint NOT NULL,
    security_contact          varchar(90),
    address                   TEXT   NOT NULL,
    schema_for_road_photo_url TEXT   NOT NULL,
    safety_precautions        TEXT
);

-- changeset danil:v0-1-6
ALTER TABLE shelter_info
    ADD FOREIGN KEY (schedules_id) REFERENCES schedules (id);
ALTER TABLE schedules
    ADD FOREIGN KEY (shelters_id) REFERENCES shelter_info (id);

-- changeset danil:v0-1-7
CREATE INDEX chat_id_user_index ON telegram_users (telegram_id);

-- changeset danil:v0-1-8
ALTER TABLE telegram_users
    ADD CONSTRAINT unique_user_telegram_id UNIQUE (telegram_id);

-- changeset danil:v0-1-9
ALTER TABLE volunteers
    ADD COLUMN second_name TEXT;






