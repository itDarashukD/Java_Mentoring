package com.example.event;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.event.model.Event;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@DisplayName("Integration End to End tests From controller to DB over Kafka Broker ")
@AutoConfigureMockMvc
class EventMessagingEndToEndIT extends AbstractTestSupporter {

    @Autowired
    MockMvc mvc;

    final String ASYNC_CREATE_EVENT_URL = "/event/asyncCreateEvent";
    final String ASYNC_UPDATE_EVENT_URL = "/event/asyncUpdateEvent";
    final String ASYNC_DELETE_EVENT_URL = "/event/asyncDeleteEvent/{event_id}";


    @DisplayName("asyncCreateEvent() - End to End test for interoperability since controller, thought Kafka topic to DB, for Event")
    @Test
    void asyncCreateEvent_dummyEvent_eventSavedInDb() throws Exception {
        mvc.perform(post(ASYNC_CREATE_EVENT_URL).contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(eventWithId555)))
	       .andDo(print())
	       .andExpect(status().isOk());

        Optional<Event> eventFromDb = getEventByEventIdJdbc(eventWithId555.getEventId());

        assertEquals(eventFromDb.get(), eventWithId555);
    }

    @DisplayName("asyncUpdateEvent() - End to End update test for interoperability since controller, thought Kafka topic to DB, for Event")
    @Test
    void asyncUpdateEvent_dummyEvent_eventUpdatedInDb() throws Exception {
        mvc.perform(put(ASYNC_UPDATE_EVENT_URL).contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(eventWithId222ToUpdate)))
	       .andDo(print())
	       .andExpect(status().isOk());

        Optional<Event> eventFromDb = getEventByEventIdJdbc(eventWithId222ToUpdate.getEventId());

        assertEquals(eventFromDb.get(), eventWithId222ToUpdate);
    }

    @DisplayName("asyncDeleteEvent() - End to End Delete test for interoperability since controller, thought Kafka topic to DB, for Event")
    @Test
    void asyncDeleteEvent_dummyEvent_eventRemovedFromDb() throws Exception {
        assertTrue(isExist(eventWithId333.getEventId()));

        mvc.perform(delete(ASYNC_DELETE_EVENT_URL, eventWithId333.getEventId()).contentType(
	       MediaType.APPLICATION_JSON))
	       .andDo(print())
	       .andExpect(status().isOk());

        assertFalse(isExist(eventWithId333.getEventId()));
    }


}

