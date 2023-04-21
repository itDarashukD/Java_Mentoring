package com.example.event.service.impl;


import com.example.event.dao.EventDao;
import com.example.event.model.Event;
import com.example.event.service.EventService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private EventDao eventDao;

    private static final int RETURNED_SUCCESS_RESULT_OF_DAO_OPERATION = 1;

    @Autowired
    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event createEvent(Event event) {
        long insertResult = eventDao.createEvent(event);
        if (insertResult != RETURNED_SUCCESS_RESULT_OF_DAO_OPERATION) {
            log.error(String.format("event with id %s was not created", event.getEventId()));
            return null;
        }
        return event;
    }

    @Override
    public Event updateEvent(Long eventId, Event event) {
        final Event eventToUpdate = getEventById(eventId);

        eventToUpdate.setEventType(event.getEventType());
        eventToUpdate.setDateTime(event.getDateTime());
        eventToUpdate.setSpeaker(event.getSpeaker());
        eventToUpdate.setTitle(event.getTitle());

        long updateResult = eventDao.updateEvent(eventToUpdate);

        if (updateResult != RETURNED_SUCCESS_RESULT_OF_DAO_OPERATION) {
            log.error(String.format("event with id %s was not updated", event.getEventId()));
            return null;
        }
        return eventToUpdate;
    }

    @Override
    public Event getEventById(long eventId) {
        return Optional.ofNullable(eventDao.getEventById(eventId))
	       .orElseThrow(() -> new IllegalArgumentException(String.format(
		      "event with id %s did not found",
		      eventId)));
    }

    @Override
    public boolean deleteEvent(long eventId) {
        long deleteResult = eventDao.deleteEvent(eventId);
        if (deleteResult != RETURNED_SUCCESS_RESULT_OF_DAO_OPERATION) {
            log.error(String.format("event with id %s was not deleted",eventId));
            return false;
        }
        return true;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventDao.getAllEvents();
    }

}
