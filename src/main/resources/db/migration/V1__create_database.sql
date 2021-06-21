create table if not exists role
(
    id   serial      not null
        constraint role_pk
            primary key,
    name varchar(25) not null
);

create table if not exists users
(
    id         serial       not null
        constraint users_pk
            primary key,
    username   varchar(25)  not null
        constraint users_username_key
            unique,
    email      varchar(50)  not null
        constraint users_email_key
            unique,
    password   varchar(100) not null,
    first_name varchar(30)  not null,
    last_name  varchar(30)  not null
);

create table if not exists user_role
(
    user_id integer not null
        constraint user_role_fk0
            references users,
    role_id integer not null
        constraint user_role_fk1
            references role,
    constraint user_role_pk
        primary key (user_id, role_id)
);

create table if not exists feature
(
    id          serial       not null
        constraint feature_pk
            primary key,
    title       varchar(100) not null,
    description varchar      not null,
    status      varchar      not null
);

create table if not exists task
(
    id              serial       not null
        constraint task_pk
            primary key,
    title           varchar(100) not null,
    description     varchar      not null,
    feature_id      integer      not null
        constraint task_fk0
            references feature,
    current_user_id integer      not null
        constraint task_fk1
            references users,
    status          varchar      not null,
    bug_id          integer
);

create table if not exists user_feature
(
    user_id    integer not null
        constraint user_feature_fk0
            references users,
    feature_id integer not null
        constraint user_feature_fk1
            references feature,
    constraint user_feature_pk
        primary key (user_id, feature_id)
);



create table if not exists bug
(
    id          serial       not null
        constraint bug_pk
            primary key,
    title       varchar(100) not null,
    description varchar      not null,
    status      varchar      not null,
    task_id     integer      not null
        constraint bug_fk0
            references task
);



