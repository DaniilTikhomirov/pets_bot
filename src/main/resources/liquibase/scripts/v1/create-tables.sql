-- liquibase formatted sql

-- changeset danil:v0-2-0
CREATE TABLE status
(
    id          serial PRIMARY KEY,
    status_name varchar(255)
);

-- changeset danil:v0-2-1
CREATE TABLE animals
(
    id          bigserial PRIMARY KEY,
    name        TEXT,
    description TEXT,
    color       varchar(255),
    status_id   int,
    age         int,
    kind        TEXT,
    photo_url   TEXT
);

-- changeset danil:v0-2-2
CREATE TABLE dog_handler
(
    id                bigserial PRIMARY KEY,
    name              TEXT        NOT NULL,
    second_name       TEXT        NOT NULL,
    contact           varchar(90) NOT NULL,
    description       TEXT,
    animals_advise_id bigint
);

-- changeset danil:v0-2-3
CREATE TABLE animals_advice
(
    id                                            bigserial PRIMARY KEY,
    rules_for_getting_animal                      TEXT NOT NULL,
    documents_for_animal                          TEXT NOT NULL,
    recommendation_for_move_animal                TEXT NOT NULL,
    recommendation_for_arrangement_for_puppy      TEXT NOT NULL,
    recommendation_for_arrangement_for_dog        TEXT NOT NULL,
    recommendation_for_arrangement_for_disability TEXT NOT NULL,
    dog_handler_advice                            TEXT NOT NULL,
    reasons_for_refusal                           TEXT NOT NULL
);

-- changeset danil:v0-2-4
ALTER TABLE dog_handler ADD FOREIGN KEY (animals_advise_id) REFERENCES animals_advice(id);

-- changeset danil:v0-2-5
ALTER TABLE dog_handler ADD COLUMN photo_url TEXT;

-- changeset danil:v0-2-6
ALTER TABLE telegram_users DROP COLUMN streak_counter;

-- changeset danil:v0-2-7
CREATE TABLE adopter (
    id bigserial PRIMARY KEY,
    name TEXT NOT NULL,
    contact varchar(90) NOT NULL,
    animal_id bigint REFERENCES animals(id),
    streak_counter int DEFAULT(0),
    send_report BOOLEAN DEFAULT(FALSE)
);

-- changeset danil:v0-2-8
ALTER TABLE animals ADD COLUMN adopter_id bigint REFERENCES adopter(id);

-- changeset danil:v0-2-9
ALTER TABLE adopter ADD COLUMN warning_counter int DEFAULT(0);

-- changeset danil:v0-2-10
ALTER TABLE adopter ADD COLUMN telegram_id bigint NOT NULL ;

-- changeset danil:v0-2-11
ALTER TABLE telegram_users ADD COLUMN name TEXT;


-- changeset danil:v0-2-12
ALTER TABLE adopter ADD COLUMN warning_streak_counter int DEFAULT (0);

-- changeset danil:v0-2-13
ALTER TABLE animals ADD COLUMN take BOOLEAN DEFAULT (FALSE);




