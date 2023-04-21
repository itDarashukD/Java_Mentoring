

CREATE OR REPLACE PROCEDURE  saveUser(_name text, _surname text, _birthdate date)
LANGUAGE plpgsql
AS $$
BEGIN
  INSERT INTO "User" (name, surname,birthdate )
    VALUES (_name, _surname, _birthdate);
END
$$;


CREATE OR REPLACE PROCEDURE updateUser(_name text, _birthdate date, _surname text)
LANGUAGE plpgsql
AS $$
 BEGIN

UPDATE "User" SET name = _name, birthdate = _birthdate WHERE surname = _surname;

END
$$;


CREATE OR REPLACE PROCEDURE deleteUser( _surname text)
LANGUAGE plpgsql
AS $$
BEGIN

DELETE FROM "User" WHERE surname = _surname;

END
$$;


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
$$ LANGUAGE plpgsql ;


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

