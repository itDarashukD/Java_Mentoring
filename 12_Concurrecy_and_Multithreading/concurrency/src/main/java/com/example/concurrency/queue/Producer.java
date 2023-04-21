package com.example.concurrency.queue;

import com.example.concurrency.experiment.ConcurrentHandle;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Value("${spring.activemq.queue}")
    String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

    public Boolean JMSConvertAndSend() {
        final String randomMessage = getRandomMessage();

        jmsTemplate.convertAndSend(queueName, randomMessage);
        log.info("{} was sent in the {} queue ", randomMessage, queueName);
        return true;
    }

    private String getRandomMessage() {
        return RandomStringUtils.randomAlphabetic(10, 50);
    }


}
