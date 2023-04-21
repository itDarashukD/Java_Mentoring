
INSERT INTO public."User"(name, surname, birthdate)
	VALUES ('testName1','testSurname1','2000-01-01');

	INSERT INTO public."User"(name, surname, birthdate)
	VALUES ('testName2','testSurname2','2002-02-02');

	INSERT INTO public."User"(name, surname, birthdate)
	VALUES ('testName3','testSurname3','2003-03-03');

	INSERT INTO public."User"(name, surname, birthdate)
	VALUES ('testname4','testSurname4','2000-04-04');


INSERT INTO public."Friendship"(user_id1, user_id2, timestamp)
	VALUES ( 1, 2,TIMESTAMP '2011-11-11 11:11:11');
	INSERT INTO public."Friendship"(user_id1, user_id2, timestamp)
	VALUES ( 3, 4, TIMESTAMP '2022-12-12 12:12:12');
	INSERT INTO public."Friendship"(user_id1, user_id2, timestamp)
	VALUES ( 5, 6, TIMESTAMP '2033-03-03 03:33:33');


INSERT INTO public."Likes"(post_id, user_id, timestamp)
	VALUES ( 1, 2,TIMESTAMP '2011-11-11 11:11:11');
	INSERT INTO public."Likes"(post_id, user_id, timestamp)
	VALUES ( 3, 4,TIMESTAMP '2022-12-12 12:12:12');
	INSERT INTO public."Likes"(post_id, user_id, timestamp)
	VALUES ( 5, 6,TIMESTAMP '2033-03-03 03:33:33');