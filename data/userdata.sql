create database productivity_app;

use productivity_app;

create table user_data (
	userid varchar(128) not null,
	name varchar(128) not null,
    username varchar(80) not null unique,
    gender varchar(50) not null,
    email varchar(128) not null,
    birthdate date not null,
    password varchar(128) not null,
    is_premium boolean default false,
	
    primary key(userid)
);