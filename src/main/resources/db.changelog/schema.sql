create table person
(
    id       uuid primary key,
    version  int,
    name     varchar(255),
    lastname varchar(255)
);

create table address
(
    person        uuid not null,
    street        varchar,
    city          varchar,
    postal_number varchar,
    street_number varchar,
    constraint address_person foreign key (person) references person

);

create table minion
(
    id             uuid primary key,
    version        int,
    name           varchar(255),
    number_of_eyes varchar(255),
    evil_master    uuid,
    description    jsonb,
    constraint FK_minion_person foreign key (evil_master) references person
);

create table color
(
    minion uuid not null,
    name   varchar(255)
);

create table toy
(
    minion   uuid not null,
    name     varchar(255),
    material varchar(255)
)
