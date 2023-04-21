
DROP SCHEMA public CASCADE;
CREATE SCHEMA IF NOT EXISTS public;

DROP TABLE IF EXISTS public.Event;
DROP TABLE IF EXISTS public.Ticket;
DROP TABLE IF EXISTS public.User;

CREATE TABLE IF NOT EXISTS public."Event"
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1, INCREMENT BY 1),
    title varchar(255),
    date date,
    CONSTRAINT "event_pk" PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public."Ticket"
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1, INCREMENT BY 1),
    event_id bigint,
    user_id bigint,
    place integer,
    category varchar(255),
    CONSTRAINT "ticket_pk" PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public."User"
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( START WITH 1, INCREMENT BY 1),
    name varchar(255),
    email varchar(255),
    CONSTRAINT "user_pk" PRIMARY KEY (id)
)
