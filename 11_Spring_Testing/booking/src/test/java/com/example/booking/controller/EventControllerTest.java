package com.example.booking.controller;


import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.TestSupporter;
import com.example.booking.model.Event;
import com.example.booking.service.impl.EventServiceImpl;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;


@DisplayName("integration test for EventController with endpoint invoking")
@AutoConfigureMockMvc
class EventControllerTest extends TestSupporter {


    @Autowired
    MockMvc mvc;
    @MockBean
    EventServiceImpl service;
    @MockBean
    JmsTemplate jmsTemplate;

    private static final String EXPECTED_CONTENT_FOR_EVENT111 = "{\"id\":1,\"title\":\"title111\",\"date\":\"2011-11-11\"}";

    @Test
    void getEventById_dummyEvent_endpointInvoked() throws Exception {
        when(service.getEventById(anyLong())).thenReturn(event111);

        mvc.perform(get("/event/getEvent/1"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(jsonPath("$.id", equalTo(1)))
	       .andExpect(jsonPath("$.title", equalTo("title111")))
	       .andExpect(jsonPath("$.date", equalTo("2011-11-11")));
    }

    @Test
    void getEventsByTitle_dummyEvent_endpointInvoked() throws Exception {
        when(service.getEventsByTitle(anyString())).thenReturn(List.of(event111));

        mvc.perform(get("/event/getEventByTitle/title111"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(jsonPath("$.[*].id", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].title", containsInAnyOrder("title111")))
	       .andExpect(jsonPath("$.[*].date", containsInAnyOrder("2011-11-11")));
    }

    @Test
    void getEventsForDay_dummyEvent_endpointInvoked() throws Exception {
        when(service.getEventsForDay(any(Date.class))).thenReturn(List.of(event111));

        mvc.perform(get("/event/getEventForDay/2011-11-11"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(jsonPath("$.[*].id", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].title", containsInAnyOrder("title111")))
	       .andExpect(jsonPath("$.[*].date", containsInAnyOrder("2011-11-11")));
    }

    @Test
    void createEvent_dummyEvent_endpointInvoked() throws Exception {
        when(service.createEvent(any(Event.class))).thenReturn(event111);

        mvc.perform(post("/event/createEvent").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(event111)))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	       .andExpect(content().string(EXPECTED_CONTENT_FOR_EVENT111));
    }


    @Test
    void updateEvent_dummyEvent_endpointInvoked() throws Exception {
        when(service.updateEvent(any(Event.class))).thenReturn(event111);

        mvc.perform(put("/event/updateEvent").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(event111)))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	       .andExpect(content().string(EXPECTED_CONTENT_FOR_EVENT111));
    }

    @Test
    void deleteEvent_dummyEventId_endpointInvoked() throws Exception {
        when(service.deleteEvent(anyLong())).thenReturn(true);

        mvc.perform(delete("/event/deleteEvent/1"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(content().string("true"));
    }

    @Test
    void asyncCreateEvent_dummyEvent_endpointInvoked() throws Exception {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(Event.class));

        mvc.perform(post("/event/asyncCreateEvent").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(event111)))
	       .andDo(print())
	       .andExpect(status().isOk());
    }


}

