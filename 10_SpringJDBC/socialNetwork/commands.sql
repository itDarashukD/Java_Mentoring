--create tables:

CREATE TABLE IF NOT EXISTS public."User"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name text COLLATE pg_catalog."default",
    surname text COLLATE pg_catalog."default",
    birthdate date,
    CONSTRAINT "User_PK" PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public."Like"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    "postId" bigint,
    "userId" bigint,
    "timestamp" timestamp without time zone,
    CONSTRAINT "like_PK" PRIMARY KEY (id),
    CONSTRAINT "like_FK" FOREIGN KEY ("userId")
        REFERENCES public."User" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

CREATE TABLE IF NOT EXISTS public."Friendship"
(
    id bigint NOT NULL,
    "userId1" bigint,
    "userId2" bigint,
    "timestamp" timestamp without time zone,
    CONSTRAINT "friendship_PK" PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public."Post"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    "userId" bigint,
    text text COLLATE pg_catalog."default",
    "timestamp" timestamp without time zone,
    CONSTRAINT "post_PK" PRIMARY KEY (id)
)

--Program should print out all names (only distinct) of users who has more than 100 friends and 100 likes in March 2025.
SELECT u.name,  count(l.user_id)
FROM "Likes" as l
LEFT JOIN "Friendship" as f
ON (l.user_id = f.user_id1)

LEFT JOIN "User" as u
ON (u.id = l.user_id)

where
l.timestamp between '2025-03-01 00:00:00.000' and '2025-04-01 00:00:00.000'
and
f.timestamp between '2025-03-01 00:00:00.000' and '2025-04-01 00:00:00.000'

GROUP BY u.name
HAVING
count(f.user_id2) >100 ;


-- Create function that will print out all names (only distinct) of users who has more than 100 friends and 100 likes in March 2025.
CREATE OR REPLACE FUNCTION get_name_of_users_with_more_100_likes_and_more_100_friends()
RETURNS TABLE (name text, count bigint) AS
$BODY$

BEGIN

   RETURN QUERY  SELECT u.name,  count(l.user_id)
   FROM "Likes" as l
   LEFT JOIN "Friendship" as f
   ON (l.user_id = f.user_id1)
   LEFT JOIN "User" as u
   ON (u.id = l.user_id)

    WHERE
    l.timestamp between '2025-03-01 00:00:00.000' AND '2025-04-01 00:00:00.000'
    AND
    f.timestamp between '2025-03-01 00:00:00.000' AND '2025-04-01 00:00:00.000'

    GROUP BY u.name
    HAVING
    COUNT(f.user_id2) >100 ;

END
$BODY$

LANGUAGE 'plpgsql' VOLATILE;

SELECT get_name_of_users_with_more_100_likes_and_more_100_friends();


-- Create stored procedure for saveUser().
CREATE OR REPLACE PROCEDURE  saveUser(_name text, _surname text, _birthdate date)
LANGUAGE plpgsql
AS $$
BEGIN
  INSERT INTO "User" (name, surname,birthdate )
    VALUES (_name, _surname, _birthdate);
END
$$;

CALL saveUser('Ali', 'Mali', '2025-03-27 08:45:27.383');



-- Create stored procedure for updateUser().
CREATE OR REPLACE PROCEDURE updateUser(_name text, _birthdate date, _surname text)
LANGUAGE plpgsql
AS $$
 BEGIN

UPDATE "User" SET name = _name, birthdate = _birthdate WHERE surname = _surname;

END
$$;

CALL updateUser('AliAli', '2025-11-11 08:45:27.383','Mali');


-- Create stored procedure for deleteUser().
CREATE OR REPLACE PROCEDURE deleteUser( _surname text)
LANGUAGE plpgsql
AS $$
BEGIN

DELETE FROM "User" WHERE surname = _surname;

END
$$;

CALL deleteUser('Mali');


-- Create function for getBySurname().
CREATE OR REPLACE FUNCTION getBySurname(_surname text)
RETURNS TABLE (
	id bigint,
      name TEXT,
    surname TEXT,
    birthdate date
  ) AS $$
#variable_conflict use_column
BEGIN

RETURN QUERY SELECT id, name,surname,birthdate FROM "User" WHERE "User".surname = _surname Limit 1;

END

SELECT * from getBySurname('fakeSurName222');


-- Create function for getAllUsers().
CREATE OR REPLACE FUNCTION getAllUsers()
RETURNS   TABLE (
	id bigint,
      name TEXT,
    surname TEXT,
    birthdate date
  ) AS $$
BEGIN

   RETURN QUERY SELECT * FROM "User" ;

END
$$ LANGUAGE plpgsql ;

SELECT * FROM getAllUsers();


-- Create SQL that will print out all surnames (only distinct) with do post more than 1000 during half of the year.
SELECT u.surname,  sum(l.post_id)
from "User" as u
LEFT JOIN "Likes" as l
ON (u.id = l.user_id)
where  l.timestamp between '2025-01-01 00:00:00.000' and '2025-06-01 00:00:00.000'
GROUP BY u.surname
HAVING sum(l.post_id)>1000 ;"


-- Create function that will print out all surnames (only distinct) with do post more than 1000 during half of the year.
CREATE OR REPLACE FUNCTION get_surname_list_with_post_count_for_half_of_year_more_1000()
RETURNS TABLE (surname text, sum numeric ) AS
$BODY$

BEGIN

   RETURN QUERY  Select u.surname,  sum(l.post_id)
   from "User" as u
   LEFT JOIN "Likes" as l
   ON (u.id = l.user_id)
   where  l.timestamp between '2025-01-01 00:00:00.000' and '2025-06-01 00:00:00.000'
   GROUP BY u.surname HAVING sum(l.post_id)>1000 ;

END
$BODY$

LANGUAGE 'plpgsql' VOLATILE;

SELECT get_surname_list_with_post_count_for_half_of_year_more_1000();



-- Create function to show all procedures
 CREATE OR REPLACE FUNCTION getAllProcedures()
 RETURNS SETOF information_schema.sql_identifier AS
$BODY$
    BEGIN

    RETURN QUERY SELECT routine_name FROM information_schema.routines WHERE routine_type = 'PROCEDURE';

    END
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

SELECT getAllProcedures();


-- Create procedure to remove all functions.
CREATE OR REPLACE PROCEDURE removeAllProcedures()
LANGUAGE plpgsql AS $$
DECLARE
   r record;
BEGIN
   FOR r IN (SELECT proname FROM pg_proc WHERE proname !~ '^pg_') LOOP
      EXECUTE 'DROP FUNCTION IF EXISTS ' || r.proname || '() CASCADE';
   END LOOP;
END
  $$;

CALL removeAllProcedures();