
--DROP SCHEMA public CASCADE;
CREATE SCHEMA IF NOT EXISTS public;

DROP TABLE IF EXISTS public.user;
DROP TABLE IF EXISTS public.permission;
DROP TABLE IF EXISTS public.secretdata;


CREATE TABLE IF NOT EXISTS public.permission
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    user_name text COLLATE pg_catalog."default",
    user_email text COLLATE pg_catalog."default",
    permission text COLLATE pg_catalog."default",

    CONSTRAINT "permission_pk" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.secretdata
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    secret_data text COLLATE pg_catalog."default",
    secret_data_hash text COLLATE pg_catalog."default",

    CONSTRAINT "secret_data_pk" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.user
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name text COLLATE pg_catalog."default",
    password text COLLATE pg_catalog."default",
    email text COLLATE pg_catalog."default",

    CONSTRAINT "user_pk" PRIMARY KEY (id)
);


INSERT INTO public."user"(name, password, email)
	VALUES ('VIEW_ADMIN','D4D4EAE597EF9B8C042DE6A687DA856A:52EEAB02376E563B088DEECAA6A1225671104E60CC566005F262FFF62E69BCFE','VIEW_ADMIN@1.by');

INSERT INTO public."user"(name, password, email)
	VALUES ('VIEW_INFO','815911A7C46569CBDE4C8C6291EAAB31:ED3E0747A3A6260230484EC40773D6E38D678C0F089A722EFDC98E162E51A53E','VIEW_INFO@1.by');



INSERT INTO public."permission"(user_name, user_email, permission)
		VALUES ('VIEW_INFO','VIEW_INFO@1.by','VIEW_INFO');

INSERT INTO public."permission"(user_name, user_email, permission)
		VALUES ('VIEW_ADMIN','VIEW_ADMIN@1.by','VIEW_ADMIN');



INSERT INTO public."secretdata"(secret_data, secret_data_hash)
	VALUES ('cdcd','3048578');


