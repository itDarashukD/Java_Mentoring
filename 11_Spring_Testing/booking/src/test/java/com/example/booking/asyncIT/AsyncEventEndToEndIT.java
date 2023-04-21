package com.example.booking.asyncIT;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.TestSupporter;
import com.example.booking.model.Event;
import java.sql.Date;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@DisplayName("integration test for AsyncActiveMQ queue")
@AutoConfigureMockMvc
class AsyncEventEndToEndIT extends TestSupporter {


    protected static final Event event444 = new Event() {{
        setId(4);
        setTitle("title444");
        setDate(Date.valueOf("2004-04-04"));
    }};

    @Autowired
    MockMvc mvc;

    @DisplayName("asyncCreateEvent() - End to End test for interoperability since controller thought AsyncActiveMQ queue to DB for Event")
    @Test
    void asyncCreateEvent_dummyEvent_eventCreatedInDb() throws Exception {
        mvc.perform(post("/event/asyncCreateEvent").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event444)))
                .andDo(print())
                .andExpect(status().isOk());

        final Optional<Event> eventFromDb = getEventByIdJdbc(event444.getId());

        assertEquals(eventFromDb.get(), event444);
    }


}

