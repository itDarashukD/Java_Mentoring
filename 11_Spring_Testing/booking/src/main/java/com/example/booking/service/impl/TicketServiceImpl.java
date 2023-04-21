package com.example.booking.service.impl;


import com.example.booking.dao.TicketDao;
import com.example.booking.model.Ticket;
import com.example.booking.service.TicketService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {


    private TicketDao ticketDao;

    @Autowired
    public TicketServiceImpl(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        ticketDao.bookTicket(ticket);

        return ticket;
    }

    @Override
    public List<Ticket> getBookedTicketsByUser(long userId) {
        List<Ticket> ticketsByUser = ticketDao.getBookedTicketsByUser(userId);

        return ticketsByUser.isEmpty() ? new ArrayList<>() : ticketsByUser;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        Ticket ticketToRemoveUser = ticketDao.getById(ticketId);

        Optional.ofNullable(ticketToRemoveUser)
	       .orElseThrow(() -> new IllegalArgumentException(String.format(
		      "ticket with ticketId %s did not found",
		      ticketId)));

        ticketToRemoveUser.setUserId(0L);

        ticketDao.update(ticketToRemoveUser);

        return true;
    }

    @Override
    public List<Ticket> getBookedTicketsByEvent(long eventId) {
        List<Ticket> ticketByEventId = ticketDao.getBookedTicketsByEvent(eventId);

        return ticketByEventId.isEmpty() ? new ArrayList<>() : ticketByEventId;
    }

}
