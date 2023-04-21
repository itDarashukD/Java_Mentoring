package com.example.booking.controller;


import com.example.booking.model.Ticket;
import com.example.booking.service.TicketService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/ticket")
@RestController
public class TicketController {


    @Value("${spring.activemq.queue.bookTicket}")
    String queueName;

    private JmsTemplate jmsTemplate;
    private TicketService service;

    @Autowired
    public TicketController(JmsTemplate jmsTemplate, TicketService service) {
        this.jmsTemplate = jmsTemplate;
        this.service = service;
    }

    @PostMapping("/bookTicket")
    public Ticket bookTicket(@RequestBody Ticket ticket) {
        return service.bookTicket(ticket);
    }

    @PostMapping("/asyncBookTicket")
    public void asyncBookTicket(@RequestBody Ticket ticket) {
        JMSConvertAndSend(ticket);
    }

    @DeleteMapping("/canselTicket/{ticketId}")
    public Boolean canselTicket(@PathVariable Long ticketId) {
        return service.cancelTicket(ticketId);
    }

    @GetMapping("/getTicketsByUser/{userId}")
    public List<Ticket> getBookedTicketsByUser(@PathVariable int userId) {

        return service.getBookedTicketsByUser(userId);
    }

    @GetMapping("/getTicketsByEvent/{eventId}")
    public List<Ticket> getBookedTicketsByEvent(@PathVariable long eventId) {

        return service.getBookedTicketsByEvent(eventId);
    }

    public Boolean JMSConvertAndSend(Ticket ticket) {
        jmsTemplate.convertAndSend(queueName, ticket);
        log.info("{} was sent in the {} queue ", ticket, queueName);

        return true;
    }

}
