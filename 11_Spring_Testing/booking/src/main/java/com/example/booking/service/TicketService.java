package com.example.booking.service;

import com.example.booking.model.Ticket;
import java.util.List;

public interface TicketService {

    Ticket bookTicket(Ticket ticket);

    boolean cancelTicket(long ticketId);

    List<Ticket> getBookedTicketsByEvent(long eventId);

    List<Ticket> getBookedTicketsByUser(long userId);

}
