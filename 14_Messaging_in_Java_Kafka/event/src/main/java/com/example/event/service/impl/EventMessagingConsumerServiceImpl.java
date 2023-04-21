package com.example.event.service.impl;


import com.example.event.dao.EventDao;
import com.example.event.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventMessagingConsumerServiceImpl {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EventDao dao;

    @KafkaListener(topics = "${spring.kafka.createTopic}")
    public void createEvent(String payload) throws JsonProcessingException {
        log.info("payload {} was received in the {} ", payload, "createEvent()");

        Event event = mapper.readValue(payload, Event.class);

        dao.createEvent(event);
    }

    @KafkaListener(topics = "${spring.kafka.updateTopic}")
    public void updateEvent(String payload) throws JsonProcessingException {
        log.info("payload {} was received in the {} ", payload, "updateEvent()");

        Event event = mapper.readValue(payload, Event.class);

        dao.updateEvent(event);
    }

    @KafkaListener(topics = "${spring.kafka.deleteTopic}")
    public void deleteEvent(String payload) {
        log.info("payload {} was received in the {} ", payload, "deleteEvent()");

        Long eventId = Long.parseLong(payload);

        dao.deleteEvent(eventId);
    }


}
