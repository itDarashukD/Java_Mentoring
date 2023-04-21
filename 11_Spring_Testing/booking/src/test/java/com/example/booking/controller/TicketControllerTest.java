package com.example.booking.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.TestSupporter;
import com.example.booking.model.Ticket;
import com.example.booking.service.TicketService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;


@DisplayName("integration test for TicketController with endpoint invoking")
@AutoConfigureMockMvc
class TicketControllerTest extends TestSupporter {


    @Autowired
    MockMvc mvc;
    @MockBean
    TicketService service;
    @MockBean
    JmsTemplate jmsTemplate;

    private static final String EXPECTED_CONTENT_FOR_TICKET111 = "{\"id\":1,\"eventId\":1,\"userId\":1,\"place\":1,\"category\":\"BAR\"}";

    @Test
    void bookTicket_dummyTicket_endpointInvoked() throws Exception {
        when(service.bookTicket(any(Ticket.class))).thenReturn(ticket111);

        mvc.perform(post("/ticket/bookTicket").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(ticket111)))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(content().string(EXPECTED_CONTENT_FOR_TICKET111));
    }

    @Test
    void getBookedTicketsByUser_dummyTicket_endpointInvoked() throws Exception {
        when(service.getBookedTicketsByUser(anyLong())).thenReturn(List.of(ticket111));

        mvc.perform(get("/ticket/getTicketsByUser/1"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(jsonPath("$.[*].id", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].eventId", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].userId", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].place", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].category", containsInAnyOrder("BAR")));
    }

    @Test
    void getBookedTicketsByEvent_dummyTicket_endpointInvoked() throws Exception {
        when(service.getBookedTicketsByEvent(anyLong())).thenReturn(List.of(ticket111));

        mvc.perform(get("/ticket/getTicketsByEvent/1"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(jsonPath("$.[*].id", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].eventId", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].userId", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].place", containsInAnyOrder(1)))
	       .andExpect(jsonPath("$.[*].category", containsInAnyOrder("BAR")));
    }

    @Test
    void canselTicket_dummyTicketId_endpointInvoked() throws Exception {
        when(service.cancelTicket(anyLong())).thenReturn(true);

        mvc.perform(delete("/ticket/canselTicket/1"))
	       .andDo(print())
	       .andExpect(status().isOk())
	       .andExpect(content().string("true"));
    }

    @Test
    void asyncBookTicket_dummyTicket_endpointInvoked() throws Exception {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(Ticket.class));

        mvc.perform(post("/ticket/asyncBookTicket").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(ticket111)))
	       .andDo(print())
	       .andExpect(status().isOk());
    }


}
