package com.example.event.service;

import com.example.event.model.Event;

public interface EventMessagingProducerService {

    Event createEvent(Event event);

    Event updateEvent( Event event);

    Boolean deleteEvent(long eventId);


}
