-- liquibase formatted sql
-- changeset Fxynos:1 dbms:h2
create table task (
    id int auto_increment primary key,
    title varchar(40) not null,
    description varchar(1000),
    completed bool not null default false
)
-- rollback drop table task;