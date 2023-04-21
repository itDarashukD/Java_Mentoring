package com.epam.ld.module2.testing;

import java.util.logging.Logger;

public class MailServer {

    private static final Logger log = Logger.getLogger(MailServer.class.getName());

    public String send(String sentMessage) {

        log.info(sentMessage);

        return sentMessage;
    }
}
