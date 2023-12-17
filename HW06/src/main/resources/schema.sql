create table if not exists authors
(
    id        bigserial,
    full_name varchar(255),
    primary key (id)
);

create table if not exists genres
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);

create table if not exists books
(
    id        bigserial,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table if not exists books_genres
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);

create table if not exists comments
(
    id        bigserial,
    book_id   bigint references books (id) on delete cascade,
    full_text varchar(255),
    primary key (id)
);