package com.example.booking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.booking.TestSupporter;
import com.example.booking.model.Category;
import com.example.booking.model.Ticket;
import com.example.booking.service.TicketService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayName("test for TicketServiceImpl.class")
class TicketServiceImplTest extends TestSupporter {


    @Autowired
    private TicketService ticketService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Ticket ticket222 = new Ticket() {{
        setId(2);
        setEventId(2);
        setUserId(2);
        setPlace(2);
        setCategory(Category.STANDARD);
    }};
    private static final Ticket ticket444 = new Ticket() {{
        setId(4);
        setEventId(4);
        setUserId(4);
        setPlace(4);
        setCategory(Category.BAR);
    }};

    @Test
    void bookTicket_dummyTicket_booked() {
        final Ticket ticket = ticketService.bookTicket(ticket444);
        assertEquals(ticket, ticket444);
    }

    @Test
    void getBookedTicketsByUser_dummyUser_bookedTickedByUserExists() {
        final List<Ticket> bookedTicketsByUser = ticketService.getBookedTicketsByUser(user111.getId());
        assertTrue(bookedTicketsByUser.contains(ticket111));
    }

    @Test
    void cancelTicket_dummyTicket_cancelled() {
        final int userIdForCancelledTicket = 0;
        final boolean isCanceled = ticketService.cancelTicket(ticket222.getId());
        assertTrue(isCanceled);

        final Ticket ticketByIdJdbc = getTicketByIdJdbc(ticket222.getId()).get();

        assertEquals(ticketByIdJdbc.getId(), ticket222.getId());
        assertEquals(ticketByIdJdbc.getCategory(), ticket222.getCategory());
        assertEquals(ticketByIdJdbc.getEventId(), ticket222.getEventId());
        assertEquals(ticketByIdJdbc.getPlace(), ticket222.getPlace());
        assertEquals(ticketByIdJdbc.getUserId(), userIdForCancelledTicket);
    }

    @Test
    void getBookedTicketsByEvent_dummyEvent_bookedTickedByEventExists() {
        final List<Ticket> bookedTicketsByEvent = ticketService.getBookedTicketsByEvent(event111.getId());
        assertTrue(bookedTicketsByEvent.contains(ticket111));
    }


}