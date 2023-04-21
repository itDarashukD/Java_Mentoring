package org.example.lfu.lfuCache;


import ch.qos.logback.classic.Logger;
import org.example.lfu.entity.Model;
import org.example.lfu.service.TimeService;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LfuCache {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LfuCache.class);

    private final TimeService timeService = new TimeService();

    private final int capacity;
    private int evictCounter = 0;
    private int minUsageCount = -1; //count of usages in freqMap ;when insert value that don't exist in catch --> return -1
    private final int initializeValue = 1;

    private final HashMap<Integer, Model> cache = new HashMap<>();
    private final HashMap<Integer, Integer> keyCounts = new HashMap<>();
    private final Map<Integer, LinkedHashSet<Integer>> frequencyMap = new ConcurrentHashMap<>();

    public LfuCache(int capacity) {
        this.capacity = capacity;
        frequencyMap.put(initializeValue, new LinkedHashSet<>());
    }

    public Model get(Model model) {
        int key = getKey(model);

        if (!cache.containsKey(key)) {
            log.info("The {} is not present in cache", model.getName());
            return null;
        }

        int count = keyCounts.get(key);
        keyCounts.put(key, count + 1);
        frequencyMap.get(count).remove(key); // remove old count value

        if (count == minUsageCount && frequencyMap.get(count).size() == 0) {
            minUsageCount++;
        }

        if (!frequencyMap.containsKey(count + 1)) {
            frequencyMap.put(count + 1, new LinkedHashSet<>());
        }

        frequencyMap.get(count + 1).add(key);
        return cache.get(key);
    }

    public Model put(Model model) {
        int usedOnes = 1;
        int firstLevelOfFrequency = 1;
        long before = System.currentTimeMillis();

        int key = getKey(model);
        if (capacity <= 0) {
            throw new IllegalArgumentException("The capacity must to be more than 0");
        }

        if (cache.containsKey(key)) {
            cache.put(key, model); //to refresh value
            return get(model); // to increase frequency of usage
        }

        if (isFull()) {
            int evict = frequencyMap.get(minUsageCount).iterator().next();
            Model tempModel = cache.get(evict);

            frequencyMap.get(minUsageCount).remove(evict);
            cache.remove(evict);
            keyCounts.remove(evict);

            evictCounter++;
            log.info("The {} was removed from cache", tempModel.getName());
        }

        cache.put(key, model);
        keyCounts.put(key, usedOnes);
        minUsageCount = usedOnes;
        frequencyMap.get(firstLevelOfFrequency).add(key);

        long after = System.currentTimeMillis();
        timeService.calculatePuttingTime(before, after);

        return model;
    }

    private int getKey(Model model) {
        return model.hashCode();
    }

    private boolean isFull() {
        return cache.size() >= capacity;
    }

    public void printEvictCounter() {
        log.info("The count of cache evictions is {}", evictCounter);
    }

}