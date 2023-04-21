package com.example.event.service;

import com.example.event.model.Event;
import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    Event updateEvent(Long eventId, Event event);

    Event getEventById(long eventId);

    boolean deleteEvent(long eventId);

    List<Event> getAllEvents();

}
