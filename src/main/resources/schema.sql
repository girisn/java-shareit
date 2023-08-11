CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(50)                             NOT NULL,
    description VARCHAR(2000),
    available   BOOLEAN,
    owner_id    BIGINT                                  NOT NULL,
    request_id  BIGINT,
    CONSTRAINT pk_item_id PRIMARY KEY (id),
    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT uq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     VARCHAR(10)                             NOT NULL,
    CONSTRAINT pk_booking_id PRIMARY KEY (id),
    CONSTRAINT fk_item_id_booking FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_booker_id FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(2000),
    requestor_id BIGINT,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_request_id PRIMARY KEY (id),
    CONSTRAINT fk_requestor_id FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(2000)                           NOT NULL,
    item_id   BIGINT,
    author_id BIGINT,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_comment_id PRIMARY KEY (id),
    CONSTRAINT fk_item_id_comment FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (id)
);