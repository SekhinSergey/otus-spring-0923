CREATE TABLE if NOT EXISTS authors (
    id bigserial,
    full_name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS genres (
    id bigserial,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS books (
    id bigserial,
    title VARCHAR(255),
    author_id bigint NOT NULL REFERENCES authors (id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS books_genres (
    book_id bigint REFERENCES books (id) ON DELETE CASCADE,
    genre_id bigint REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, genre_id)
);

CREATE TABLE if NOT EXISTS users (
    id bigserial,
    email VARCHAR(255),
    roles VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS tokens (
    id bigserial,
    user_id bigint NOT NULL REFERENCES users (id),
    token VARCHAR(10000),
    revoked BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS comments (
    id bigserial,
    text VARCHAR(255),
    book_id bigint NOT NULL REFERENCES books (id) ON DELETE CASCADE,
    user_id bigint NOT NULL REFERENCES users (id),
    PRIMARY KEY (id)
);
