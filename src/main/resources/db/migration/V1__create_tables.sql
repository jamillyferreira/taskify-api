CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(180) NOT NULL ,
    email VARCHAR(255) NOT NULL UNIQUE ,
    password VARCHAR(255) NOT NULL ,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL ,
    description VARCHAR(255),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    due_date DATE,
    priority VARCHAR(10) NOT NULL DEFAULT 'MEDIUM'
        CHECK ( priority IN ('LOW', 'MEDIUM', 'HIGH')),
    created_by VARCHAR(180) NOT NULL ,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL ,
    deleted_at TIMESTAMP
);

