-- creating tables

CREATE TABLE IF NOT EXISTS public.exam_result
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    student_surname text COLLATE pg_catalog."default",
    subject text COLLATE pg_catalog."default",
    mark smallint,
    CONSTRAINT "exam_result_PK" PRIMARY KEY (id),
    CONSTRAINT "exam_results_FK" FOREIGN KEY (id)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.subjects
(
id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
subject_name text COLLATE pg_catalog."default",
tutor_name text COLLATE pg_catalog."default",
CONSTRAINT "subjects_PK" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.student
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name text COLLATE pg_catalog."default" NOT NULL,
    surname text COLLATE pg_catalog."default" NOT NULL,
    phone_number text COLLATE pg_catalog."default",
    primary_skill text COLLATE pg_catalog."default",
    date_of_birth date,
    created_datetime timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_datetime timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "student_PK" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.student_address
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    student_surname text,
    address text,
    CONSTRAINT "Address_PK" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.student_address_reserve
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    student_surname text,
    address text,
    CONSTRAINT "student_address_reserve_PK" PRIMARY KEY (id)
);


--show indexes

SELECT
tablename,indexname,indexdef
FROM
pg_indexes
WHERE
schemaname = 'public';

CREATE EXTENSION pg_trgm;
CREATE EXTENSION btree_gist;
CREATE EXTENSION btree_gin;

Create index b_tree  on student (id);
Create index hash  on student using hash(id);
Create index gin_index  on student using gin(name);
Create index gist  on student using gist(name);


--function to set now() time during update some value in table :

     CREATE  FUNCTION update_updated_datatime_user_task()
     RETURNS TRIGGER AS $$
     BEGIN
         NEW.updated_datetime = now();
        RETURN NEW;
     END;
      $$ language 'plpgsql';

   CREATE TRIGGER update_student_task_updated_datatime
       BEFORE UPDATE
      ON
           student
       FOR EACH ROW
   EXECUTE PROCEDURE update_updated_datatime_user_task();


-- avoid '@,#,$' punctuation for 'name' column in 'student' table :

     Alter table student
     Add constraint avoid_some_punctuation
     CHECK ( name !~ '[@#$]' );


-- do snapshot for DB :

pg_dump -h localhost -U postgres -d postgres > C:\Users\Dzmitry_Darashuk\Desktop\courses\Mentoring\9_DataBases\mentoring\mentoring\backupDB.sql


-- Create function that will return average mark for input user.

 CREATE OR REPLACE FUNCTION  get_avarage_mark_for_student(_surname text)
    RETURNS integer AS
    $BODY$
    DECLARE
      avarage_mark integer;
    BEGIN
  		SELECT INTO avarage_mark AVG(mark) FROM exam_result where student_surname = _surname;
		RETURN avarage_mark;
    END;
    $BODY$
  LANGUAGE 'plpgsql' ;

SELECT get_avarage_mark_for_student('firstSurname6');


-- Create function that will return average mark for input subject name.

 CREATE OR REPLACE FUNCTION  get_avarage_mark_for_subject(_subject text)
    RETURNS integer AS
    $BODY$
    DECLARE
      avarage_mark integer;
    BEGIN
  		SELECT INTO avarage_mark AVG(mark) FROM exam_result where subject =_subject;
		RETURN avarage_mark;
    END;
    $BODY$
  LANGUAGE 'plpgsql' ;

SELECT get_avarage_mark_for_subject('Math');


-- Create function that will return student at "red zone" (red zone means at least 2 marks <=3).

CREATE OR REPLACE FUNCTION get_red_zone_student()
  RETURNS SETOF student AS
$BODY$
 DECLARE
       _surname text;
    BEGIN

	SELECT into _surname (SELECT student_surname from exam_result where mark<=3 GROUP BY student_surname having count(mark)>=2 LIMIT 1);

    RETURN QUERY SELECT * FROM student WHERE surname=_surname LIMIT 1;

    END
$BODY$
LANGUAGE 'plpgsql' VOLATILE;

SELECT get_red_zone_student();

-- Do not give access to update any information inside it.
-- Hint: you can create trigger that will reject any update operation for target table,
-- but save new row with updated (merged with original) data into separate table..

     REVOKE ALL ON TABLE TEST FROM CURRENT_USER;
     GRANT INSERT ON TABLE TEST TO CURRENT_USER;
     GRANT SELECT ON TABLE TEST TO CURRENT_USER;

     	CREATE OR REPLACE FUNCTION reject_and_insert()
         RETURNS TRIGGER AS $$
         BEGIN

         INSERT INTO public.student_address_reserve (student_surname, address) VALUES (OLD.student_surname, NEW.address);

         RETURN NULL;
         END;
          $$ language 'plpgsql';

             CREATE TRIGGER reject_update_operation_and_insert_into_new_table
         BEFORE UPDATE
         ON  student_address
         FOR EACH ROW
      	EXECUTE PROCEDURE reject_and_insert();
