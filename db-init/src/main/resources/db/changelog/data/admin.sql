
--changeset bukazoid:2021-04-21--0001-admin
insert into users
(
   user_id,
   user_login,
   user_type,
   user_password,
   create_time
)
values
(
   md5(random () ::text || clock_timestamp () ::text)::uuid,
   'admin',
   'ADMIN',
   'admin',
   now()
);