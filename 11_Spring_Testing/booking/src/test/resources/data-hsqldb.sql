
INSERT INTO public."Event"(id, title, date)
  	VALUES (1, 'title111', '2011-11-11');
  		INSERT INTO public."Event"(id, title, date)
  	VALUES (2, 'title222', '2022-02-02');
  		INSERT INTO public."Event"(id, title, date)
  	VALUES (3, 'title333', '2003-03-03');


INSERT INTO public."Ticket"(id, event_id, user_id, place, category)
	VALUES (1, 1, 1, 1, 'BAR');
	INSERT INTO public."Ticket"(id, event_id, user_id, place, category)
	VALUES (2, 2, 2, 2, 'STANDARD');
	INSERT INTO public."Ticket"(id, event_id, user_id, place, category)
	VALUES (3, 3, 3, 3, 'PREMIUM');


INSERT INTO public."User"(id, name, email)
	VALUES (1, 'name111', 'email@111.com');
INSERT INTO public."User"(id, name, email)
	VALUES (2, 'name222', 'email@222.com');
INSERT INTO public."User"(id, name, email)
	VALUES (3, 'name333', 'email@333.com');
