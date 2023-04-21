package com.example.booking.asyncIT;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.TestSupporter;
import com.example.booking.model.Ticket;
import com.example.booking.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@DisplayName("integration test for AsyncActiveMQ queue")
@AutoConfigureMockMvc
class AsyncTicketListenerIIT extends TestSupporter {


    @Autowired
    MockMvc mvc;

    @MockBean
    private TicketServiceImpl ticketService;

    @DisplayName("asyncBookedTicket() - integration test sent Ticket to AsyncActiveMQ queue and receive in @Listener method")
    @Test
    void asyncBookedTicket_dummyTicket_asyncListenerMethodWasInvoked() throws Exception {
        when(ticketService.bookTicket(any(Ticket.class))).thenReturn(ticket111);

        mvc.perform(post("/ticket/asyncBookTicket").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(ticket111)))
	       .andDo(print())
	       .andExpect(status().isOk());

        verify(ticketService, times(1)).bookTicket(ticket111);
    }


}

