package org.example.lfu.service;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TimeService {

    private static final Logger log = (Logger) LoggerFactory.getLogger(TimeService.class);
    private static final Double DEFAULT_VALUE = 0.0;

    private final List<Long> spanList = new ArrayList<>();

    public void calculatePuttingTime(long before, long after) {
        Long timeSpan = after - before;

        spanList.add(timeSpan);

        Double average = calculateAveragePuttingTime();
        log.info("The average time of put() to cache operation after {} operations is {}", spanList.size(), average);
    }

    private Double calculateAveragePuttingTime() {
        return spanList.stream().mapToDouble(x -> x).average().orElse(DEFAULT_VALUE);
    }
}
