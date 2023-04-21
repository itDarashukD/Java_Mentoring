package com.example.booking.service;


import com.example.booking.model.Event;
import com.example.booking.model.Ticket;

public interface AsynchronousBookingService {

    void receive(Ticket ticket);

    void receive(Event event);

}
