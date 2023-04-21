package com.example.concurrency.objectPool;

import com.example.concurrency.experiment.ConcurrentHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockingObjectPool {

    private static final Logger log = LoggerFactory.getLogger(BlockingObjectPool.class);
    public static final List<String> objects = new ArrayList<>();

    private int size;
    private ReentrantLock lock;
    private Condition poolIsFull;
    private Condition poolIsEmpty;

    public BlockingObjectPool(int size) {
        this.size = size;
        lock = new ReentrantLock();
        poolIsFull = lock.newCondition();
        poolIsEmpty = lock.newCondition();
    }

    public static void main(String[] args) {
        final BlockingObjectPool pool = new BlockingObjectPool(5);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable addValues = () -> {
	   while (true) {
	       final String random = RandomStringUtils.randomAlphabetic(10, 50);
	       pool.putToPool(random);
	   }
        };

        Runnable getValue = () -> {
	   while (true) {
	       pool.getFomPool();
	   }
        };

        executor.execute(addValues);
        executor.execute(getValue);
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public String getFomPool() {
        lock.lock();
        try {
	   while (objects.isEmpty()) {
	       log.info("getFomPool() : the Object pool is empty, can't get any object from the pool ");
	       poolIsEmpty.await();
	   }
	   poolIsFull.signal();

	   final String object = objects.stream().findAny().get();
	   log.info("getFomPool()  : {}, pool size : {}", object, objects.size());

	   objects.remove(object);
	   return object;
        } catch (InterruptedException e) {
	   throw new RuntimeException(e);
        } finally {
	   lock.unlock();
        }
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public void putToPool(String object) {
        lock.lock();

        try {
	   if (objects.size() >= size) {
	       log.info("putToPool() : the Objects pool is full, can't put {} to the pool ", object);

	       poolIsFull.await();
	   }
	   objects.add(object);
	   log.info("putToPool()  : {}, pool size : {}", object, objects.size());

	   poolIsEmpty.signal();
        } catch (InterruptedException e) {
	   throw new RuntimeException(e);
        } finally {
	   lock.unlock();
        }
    }


}
