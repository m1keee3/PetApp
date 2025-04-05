CREATE TABLE owners (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255),
                        birth_date DATE
);

CREATE TYPE pet_color AS ENUM ('BLACK', 'WHITE', 'BROWN', 'GOLDEN');

CREATE TABLE pets (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255),
                      birth_date DATE,
                      breed VARCHAR(255),
                      color pet_color,
                      owner_id INTEGER REFERENCES owners(id)
);

CREATE TABLE pet_friends (
                             pet_id INTEGER REFERENCES pets(id),
                             friend_id INTEGER REFERENCES pets(id)
);
