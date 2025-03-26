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

create table events (
	event_id varchar(100) primary key,
    user_id varchar(100),
    title varchar(100),
    time_from varchar(20),
    time_to varchar(20),
    event_date date not null,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_data(userid) ON DELETE CASCADE
);

CREATE TABLE todos (
  todo_id VARCHAR(36) PRIMARY KEY,
  user_id VARCHAR(36),
  content TEXT,
  CONSTRAINT fk2_user FOREIGN KEY (user_id) REFERENCES user_data(userid)
);