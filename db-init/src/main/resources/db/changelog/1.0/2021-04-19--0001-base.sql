--liquibase formatted sql
--changeset bukazoid:2021-05-07--0001-base
CREATE TABLE users
(	
   user_id uuid,
   user_login VARCHAR (32) NOT NULL,
   user_type VARCHAR (16) NOT NULL,
   user_password VARCHAR (72) NOT NULL,--length according to https://dzone.com/articles/be-aware-that-bcrypt-has-a-maximum-password-length#:~:text=Bcrypt%20is%20a%20popular%20password,length%20to%2050%2D72%20bytes.
   create_time timestamp NOT NULL,
   PRIMARY KEY (user_id),
   UNIQUE (user_login)
);
