
DROP SCHEMA public CASCADE;
CREATE SCHEMA IF NOT EXISTS public;

DROP TABLE IF EXISTS public.Event;


CREATE TABLE IF NOT EXISTS public."Event"
(
    event_id bigint NOT NULL,
    title varchar(255),
    place varchar(255),
    speaker varchar(255),
    event_type varchar(255),
    date_time timestamp without time zone,
    CONSTRAINT "event_pk" PRIMARY KEY (event_id)
)
