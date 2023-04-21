package com.example.concurrency.experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentHandle {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentHandle.class);
    private static Map<Integer, Integer> integerMap = new HashMap<>();
//      private static final Map<Integer, Integer> integerMap = new ConcurrentHashMap<>();
//    private static final Map<Integer, Integer> syncIntegerMap = Collections.synchronizedMap(integerMap);


    public static void main(String[] args) throws InterruptedException {
        Thread addValues = new Thread(() -> {
	   log.info("thread 'addValues' has started");

	   for (int i = 0; i < 2000; i++) {
	       new ConcurrentHandle().addValuesToMap();
	   }
        });

        Thread sumValues = new Thread(() -> {
	   log.info("thread 'sumValues' has started");

	   for (int i = 0; i < 2000; i++) {
	       try {
		  new ConcurrentHandle().sumMapValues();
	       } catch (InterruptedException e) {
		  throw new RuntimeException(e);
	       }
	   }
        });
        addValues.start();
        sumValues.start();
    }


    void addValuesToMap() {
        Random random = new Random();
        final int randomKey = random.ints(1, 10000).findAny().getAsInt();
        final int randomValue = random.ints(1, 10000).findAny().getAsInt();

        log.info(String.format("thread 'addValues' : %d == %d %n", randomKey, randomValue));

        integerMap.put(randomKey, randomValue);
    }

    void sumMapValues() throws InterruptedException {
        Thread.sleep(50);
        integerMap.forEach((key, value) -> {
	   log.info(String.format("thread 'sumValues'  : sum == %d %n", key + value));
        });
    }


}
