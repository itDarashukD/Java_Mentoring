package com.example.booking.controller;


import com.example.booking.model.Event;
import com.example.booking.service.impl.EventServiceImpl;
import java.sql.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/event")
@RestController
public class EventController {


    @Value("${spring.activemq.queue.createEvent}")
    String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private EventServiceImpl service;


    @GetMapping("getEvent/{eventId}")
    public Event getEventById(@PathVariable long eventId) {
        return service.getEventById(eventId);
    }

    @GetMapping("getEventByTitle/{title}")
    public List<Event> getEventsByTitle(@PathVariable String title) {
        return service.getEventsByTitle(title);
    }

    @GetMapping("getEventForDay/{date}")
    public List<Event> getEventsForDay(@PathVariable String date) {
        final List<Event> eventsForDay = service.getEventsForDay(Date.valueOf(date));
        return eventsForDay;
    }

    @PostMapping("createEvent")
    public Event createEvent(@RequestBody Event event) {
        return service.createEvent(event);
    }

    @PostMapping("asyncCreateEvent")
    public Boolean asyncCreateEvent(@RequestBody Event event) {
        return JMSConvertAndSend(event);
    }

    @PutMapping("updateEvent")
    public Event updateEvent(@RequestBody Event event) {
        return service.updateEvent(event);
    }

    @DeleteMapping("deleteEvent/{eventId}")
    public Boolean deleteEvent(@PathVariable long eventId) {
        return service.deleteEvent(eventId);
    }


    public Boolean JMSConvertAndSend(Event event) {
        jmsTemplate.convertAndSend(queueName, event);
        log.info("{} was sent in the {} queue ", event, queueName);

        return true;
    }
}
