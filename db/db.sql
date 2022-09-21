CREATE database quarkus-social;
CREATE TABLE USERS
(
    id   bigserial    not NULL PRIMARY key,
    name VARCHAR(100) not null,
    age  INTEGER      not null
);


CREATE TABLE POSTS
(
    id bigserial not NULL PRIMARY key,
    post_text varchar(150) not NULL,
    dateTime timestamp,
    user_id bigint not null references USERS(id)
)
