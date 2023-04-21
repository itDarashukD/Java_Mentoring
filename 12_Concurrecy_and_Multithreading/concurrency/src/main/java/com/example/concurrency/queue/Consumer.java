package com.example.concurrency.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    @JmsListener(destination = "bus", containerFactory = "factory")
    public void receive(String random) {
        log.info("IN Listener --> received object : {}", random);
    }


}
