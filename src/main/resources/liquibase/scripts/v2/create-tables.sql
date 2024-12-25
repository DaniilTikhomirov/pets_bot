-- liquibase formatted sql


-- changeset danil:v0-3-0
CREATE TABLE owners(
    id bigserial PRIMARY KEY,
    telegram_id bigint NOT NULL,
    name TEXT NOT NULL
)

-- changeset danil:v0-3-1
CREATE TABLE reports(
    id bigserial PRIMARY KEY,
    adopter_id bigint REFERENCES adopter(id) NOT NULL,
    animal_id bigint REFERENCES animals(id) NOT NULL,
    volunteer_id bigint REFERENCES volunteers(id) NOT NULL,
    photo_url TEXT NOT NULL,
    text TEXT NOT NULL,
    accepted BOOLEAN DEFAULT (FALSE)
)

-- changeset danil:v0-3-2
ALTER TABLE reports ALTER COLUMN photo_url DROP NOT NULL;

-- changeset danil:v0-3-3
ALTER TABLE animals ADD COLUMN cat BOOLEAN DEFAULT(FALSE);
