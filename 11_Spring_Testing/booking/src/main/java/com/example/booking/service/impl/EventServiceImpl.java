package com.example.booking.service.impl;

import com.example.booking.dao.EventDao;
import com.example.booking.model.Event;
import com.example.booking.service.EventService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private EventDao eventDao;

    @Autowired
    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event getEventById(long eventId) {
        return Optional.ofNullable(eventDao.getEventById(eventId))
	       .orElseThrow(() -> new IllegalArgumentException(String.format(
		      "event with id %s did not found",
		      eventId)));
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        List<Event> eventsByTitle = eventDao.getEventsByTitle(title);

        return eventsByTitle.isEmpty() ? new ArrayList<>() : eventsByTitle;
    }

    @Override
    public List<Event> getEventsForDay(Date day) {
        List<Event> eventsForDay = eventDao.getEventsForDay(day.toString());

        return eventsForDay.isEmpty() ? new ArrayList<>() : eventsForDay;
    }

    @Override
    public Event createEvent(Event event) {
        eventDao.createEvent(event);

        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        eventDao.updateEvent(event);

        return event;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        eventDao.deleteEvent(eventId);

        return true;
    }

}
