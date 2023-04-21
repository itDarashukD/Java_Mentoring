package com.example.event.controller;


import com.example.event.model.Event;
import com.example.event.service.EventMessagingProducerService;
import com.example.event.service.impl.EventServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EventServiceImpl service;

    @Autowired
    private EventMessagingProducerService messagingService;


    @GetMapping("getEvent/{eventId}")
    public Event getEventById(@PathVariable long eventId) {
        return service.getEventById(eventId);
    }

    @GetMapping("getAllEvents")
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @PostMapping("createEvent")
    public Event createEvent(@RequestBody Event event) {
        return service.createEvent(event);
    }

    @PutMapping("updateEvent/{eventId}")
    public Event updateEvent(@RequestBody Event event, @PathVariable long eventId) {
        return service.updateEvent(eventId, event);
    }

    @DeleteMapping("deleteEvent/{eventId}")
    public Boolean deleteEvent(@PathVariable long eventId) {
        return service.deleteEvent(eventId);
    }

    @PostMapping("asyncCreateEvent")
    public Event asyncCreateEvent(@RequestBody Event event) {
        return messagingService.createEvent(event);
    }

    @PutMapping("asyncUpdateEvent")
    public Event asyncUpdateEvent(@RequestBody Event event) {
        return messagingService.updateEvent(event);
    }

    @DeleteMapping("asyncDeleteEvent/{eventId}")
    public Boolean asyncDeleteEvent(@PathVariable long eventId) {
        return messagingService.deleteEvent(eventId);
    }

}
