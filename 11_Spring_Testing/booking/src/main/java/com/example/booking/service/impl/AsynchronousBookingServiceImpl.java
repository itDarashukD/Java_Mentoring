package com.example.booking.service.impl;


import com.example.booking.model.Event;
import com.example.booking.model.Ticket;
import com.example.booking.service.AsynchronousBookingService;
import com.example.booking.service.EventService;
import com.example.booking.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsynchronousBookingServiceImpl implements AsynchronousBookingService {


    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventService eventService;

    @Override
    @JmsListener(destination = "ticketBooking", containerFactory = "factory")
    public void receive(Ticket ticket) {
        log.info("IN Listener --> received object : {}", ticket);

        ticketService.bookTicket(ticket);
    }

    @Override
    @JmsListener(destination = "eventCreating", containerFactory = "factory")
    public void receive(Event event) {
        log.info("IN class Listener receive() : object received : {}", event);

        eventService.createEvent(event);
    }

}
