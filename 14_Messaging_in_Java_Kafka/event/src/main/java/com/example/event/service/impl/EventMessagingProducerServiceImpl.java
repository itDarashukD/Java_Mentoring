package com.example.event.service.impl;


import com.example.event.model.Event;
import com.example.event.service.EventMessagingProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventMessagingProducerServiceImpl implements EventMessagingProducerService {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${spring.kafka.createTopic}")
    private String createTopic;

    @Value("${spring.kafka.updateTopic}")
    private String updateTopic;

    @Value("${spring.kafka.deleteTopic}")
    private String deleteTopic;


    @Override
    public Event createEvent(Event event) {
        try {
	   kafkaTemplate.send(createTopic, mapper.writeValueAsString(event));
	   log.info("{} was sent in the {} topic ", event, createTopic);

        } catch (JsonProcessingException e) {
	   throw new RuntimeException(e);
        }
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        try {
	   kafkaTemplate.send(updateTopic, mapper.writeValueAsString(event));
	   log.info("{} was sent in the {} topic ", event, createTopic);
        } catch (JsonProcessingException e) {
	   throw new RuntimeException(e);
        }
        return event;
    }

    @Override
    public Boolean deleteEvent(long eventId) {
        kafkaTemplate.send(deleteTopic, String.valueOf(eventId));
        log.info("{} was sent in the {} topic ", eventId, createTopic);

        return true;
    }

}
