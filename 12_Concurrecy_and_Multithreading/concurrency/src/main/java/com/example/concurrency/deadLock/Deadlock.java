package com.example.concurrency.deadLock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Deadlock {

    public static final List<Integer> integers = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(Deadlock.class);

    public static void main(String[] args) {

        Thread addValues = new Thread(() -> {
	   Thread.currentThread().setName("addValues");
	   Random random = new Random();

	   log.info(String.format("thread %s has started ", Thread.currentThread().getName()));
	   while (true) {
	       synchronized (integers) {
		  final int randomInt =
			 random.ints(1, 100).findAny().getAsInt();
		  log.info(String.format("random : $d ", randomInt));

		  integers.add(randomInt);
	       }
	   }
        });

        Thread printValues = new Thread(() -> {

	   try {
	       Thread.currentThread().setName("printValues");
	       log.info(String.format("thread %s has started ", Thread.currentThread().getName()));
	       Thread.sleep(100);
	   } catch (InterruptedException e) {
	       throw new RuntimeException(e);
	   }

	   while (true) {
	       synchronized (integers) {
		  final Integer sum = integers.stream().reduce(0, Integer::sum);
		  log.info(String.format("sum = %d%n", sum));
	       }
	   }
        });

        Thread squareRootValues = new Thread(() -> {

	   try {
	       Thread.currentThread().setName("squareRootValues");
	       log.info(String.format("thread %s has started ", Thread.currentThread().getName()));
	       Thread.sleep(100);
	   } catch (InterruptedException e) {
	       throw new RuntimeException(e);
	   }

	   while (true) {
	       synchronized (integers) {
		  final double sumOfSquares = integers.stream()
			 .map(integer -> Math.pow(integer, 2))
			 .collect(Collectors.toList())
			 .stream()
			 .reduce(0.0, Double::sum);

		  final double sqrt = Math.sqrt(sumOfSquares);
		  log.info(String.format("squareRoot  = %,.2f", sqrt));
	       }
	   }
        });

        addValues.start();
        printValues.start();
        squareRootValues.start();
    }


}
