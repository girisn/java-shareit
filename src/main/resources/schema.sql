CREATE TABLE IF NOT EXISTS users (
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    email   CHARACTER VARYING NOT NULL,
    name    CHARACTER VARYING NOT NULL,
    CONSTRAINT email_unique UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS items (
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    name        CHARACTER VARYING NOT NULL,
    description CHARACTER VARYING,
    available   BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_items_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    start_time   TIMESTAMP,
    end_time     TIMESTAMP,
    status  CHARACTER VARYING NOT NULL,
    item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_date TIMESTAMP,
    CONSTRAINT fk_bookings_item_id FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT fk_bookings_users_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS item_comments (
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    text        VARCHAR NOT NULL,
    author_id   BIGINT NOT NULL,
    item_id     BIGINT NOT NULL,
    CONSTRAINT fk_comments_items_id FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT fk_comments_users_id FOREIGN KEY (author_id) REFERENCES users(id)
);