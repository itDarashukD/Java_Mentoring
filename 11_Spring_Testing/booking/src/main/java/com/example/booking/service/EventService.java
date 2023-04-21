package com.example.booking.service;

import com.example.booking.model.Event;
import java.util.Date;
import java.util.List;

public interface EventService {

    Event getEventById(long eventId);

    List<Event> getEventsByTitle(String title);

    List<Event> getEventsForDay(Date day);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    boolean deleteEvent(long eventId);

}
