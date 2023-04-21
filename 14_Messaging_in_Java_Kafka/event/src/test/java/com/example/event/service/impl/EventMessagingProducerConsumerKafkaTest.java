package com.example.event.service.impl;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.example.event.AbstractTestSupporter;
import com.example.event.dao.EventDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


@DisplayName("Integration test, Producer should send Event to Kafka, Consumer should receive event ")
public class EventMessagingProducerConsumerKafkaTest extends AbstractTestSupporter {


    @Autowired
    private EventMessagingProducerServiceImpl producer;
    @InjectMocks
    private EventMessagingConsumerServiceImpl consumer;
    @MockBean
    private EventDao dao;


    @DisplayName("createEvent(), Producer should send Event, and Consumer should receive it and invoke createEvent() for dao layer ")
    @Test
    void createEvent_dummyEvent_daoLayerInvokedInListener() {
        producer.createEvent(eventWithId444);

        verify(dao, timeout(10000).times(1)).createEvent(eventWithId444);
    }

    @DisplayName("updateEvent(), Producer should send Event, and Consumer should receive it and invoke updateEvent() for dao layer ")
    @Test
    void updateEvent_dummyEvent_daoLayerInvokedInListener() {
        producer.updateEvent(eventWithId444);

        verify(dao, timeout(10000).times(1)).updateEvent(eventWithId444);
    }

    @DisplayName("deleteEvent(), Producer should send Event, and Consumer should receive it and invoke deleteEvent() for dao layer ")
    @Test
    void deleteEvent_dummyEvent_daoLayerInvokedInListener() {
        producer.deleteEvent(eventWithId444.getEventId());

        verify(dao, timeout(70000).times(1)).deleteEvent(eventWithId444.getEventId());
    }


}