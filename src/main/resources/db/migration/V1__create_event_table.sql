CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    image_url VARCHAR(300),
    event_url VARCHAR(100),
    date TIMESTAMP NOT NULL,
    is_remote BOOLEAN NOT NULL
);