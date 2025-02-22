use beers;

create table styles
(
    style_id int auto_increment primary key,
    name     varchar(50) not null
);

create table users
(
    user_id    int auto_increment primary key,
    username   varchar(50) not null,
    password   varchar(50) not null,
    first_name varchar(50) not null,
    last_name  varchar(50) not null,
    email      varchar(50) not null,
    is_admin boolean     not null
);

create table beers
(
    beer_id    int auto_increment primary key,
    name       varchar(50)    not null,
    abv        decimal(10, 1) not null,
    style_id   int            not null,
    created_by int            not null,
    photo_url  varchar(320)   DEFAULT 'img/beers/default.jpg',
    constraint beers_styles_style_id_fk
        foreign key (style_id) references styles (style_id),
    constraint beers_users_user_id_fk
        foreign key (created_by) references users (user_id)
);

create table users_beers
(
    user_id int        not null,
    beer_id int        not null,
    drunk   boolean not null default 0,
    constraint users_beers_beers_beer_id_fk
        foreign key (beer_id) references beers (beer_id)
            on delete cascade,
    constraint users_beers_users_user_id_fk
        foreign key (user_id) references users (user_id)
);