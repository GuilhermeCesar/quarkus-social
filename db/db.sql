CREATE TABLE quarkus-social;
CREATE TABLE USERS
(
    id   bigserial    not NULL PRIMARY key,
    name VARCHAR(100) not null,
    age  INTEGER      not null
);