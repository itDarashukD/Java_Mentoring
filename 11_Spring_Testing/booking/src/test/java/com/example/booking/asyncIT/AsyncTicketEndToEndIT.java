package com.example.booking.asyncIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.TestSupporter;
import com.example.booking.model.Category;
import com.example.booking.model.Ticket;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("integration test for AsyncActiveMQ queue")
@AutoConfigureMockMvc
class AsyncTicketEndToEndIT extends TestSupporter {


    @Autowired
    MockMvc mvc;

    protected static final Ticket ticket444 = new Ticket() {{
        setId(4);
        setEventId(4);
        setUserId(4);
        setPlace(4);
        setCategory(Category.BAR);
    }};

    @DisplayName("asyncBookTicket() - End to End test for interoperability since controller thought AsyncActiveMQ queue to DB for Ticket")
    @Test
    void asyncBookTicket_dummyEvent_tickedWasBooked() throws Exception {
        mvc.perform(post("/ticket/asyncBookTicket").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(ticket444)))
	       .andDo(print())
	       .andExpect(status().isOk());

        final Optional<Ticket> eventFromDb = getTicketByIdJdbc(ticket444.getId());

        assertEquals(eventFromDb.get(), ticket444);
    }

}

