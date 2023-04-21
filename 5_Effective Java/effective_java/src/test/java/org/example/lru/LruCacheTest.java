package org.example.lru;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.cache.LoadingCache;
import org.example.lfu.entity.Model;
import org.example.lfu.lfuCache.LfuCache;
import org.example.logAppender.MemoryAppender;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@DisplayName("Tests for LruCache class")
@ExtendWith(MockitoExtension.class)
class LruCacheTest {

    @InjectMocks
    private LruCache lruCache;

    private static LoadingCache<String, Model> cache;
    private static final Model model1 = new Model("name1");
    private static final Model model2 = new Model("name2");
    private static final Model model3 = new Model("name3");
    private static final Model model4 = new Model("name4");
    private static final Long cacheMaximumSize = 3L;
    private static final Long expireAfterSeconds = 1L;
    private static MemoryAppender memoryAppender;

    @BeforeAll
    static void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(LfuCache.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();

    }

    @DisplayName("get(), is LFU cache work correct when cache miss")
    @Test
    void get_whenValueMissed_modelAddedToCacheFromLoadMethod() throws ExecutionException {
        cache = lruCache.createCache(cacheMaximumSize, expireAfterSeconds);

        assertEquals(0, cache.size());
        Model expected = cache.get(model1.getName());

        assertEquals(expected.getName(), model1.getName());
        assertEquals(1, cache.size());

    }

    @DisplayName("put() is LFU cache work correct when cache reach max size")
    @Test
    void put_whenCacheReachMaxSizeThenMustDoEviction_sufficientEntriesWereEvicted() {
        cache = lruCache.createCache(cacheMaximumSize, expireAfterSeconds);

        assertEquals(0, cache.size());

        cache.put(model1.getName(), model1);
        cache.put(model2.getName(), model2);
        cache.put(model3.getName(), model3);
        cache.put(model4.getName(), model4);

        assertEquals(cache.size(), 3);
        assertTrue(memoryAppender.contains("Removed entry: name1  --> Model(name=name1)", Level.INFO));
        assertTrue(memoryAppender.contains("Cause: SIZE", Level.INFO));

    }

    @DisplayName("put() is LFU cache work correct when time expired")
    @Test
    void put_whenTimeExpiredMustEvictValues_entriesWereEvicted() throws ExecutionException, InterruptedException {
        cache = lruCache.createCache(cacheMaximumSize, expireAfterSeconds);

        assertEquals(0, cache.size());
        cache.put(model1.getName(), model1);
        assertEquals(1, cache.size());

        Thread.sleep(2000);

        cache.get(model1.getName());

        assertTrue(memoryAppender.contains("Removed entry: name1  --> Model(name=name1)", Level.INFO));
        assertTrue(memoryAppender.contains("Cause: EXPIRED", Level.INFO));

    }

    @DisplayName("put() LFU cache work correct when uploaded a map of values")
    @Test
    void put_addEntriesToCacheFromMapByUsingPutAll_allValuesWereAdded() {
        cache = lruCache.createCache(cacheMaximumSize, expireAfterSeconds);

        assertEquals(0, cache.size());

        Map<String, Model> map = new HashMap<>();
        map.put(model1.getName(), model1);
        map.put(model2.getName(), model2);

        cache.putAll(map);

        assertEquals(2, cache.size());

    }

    @DisplayName("Is LFU cache work correct with Removal Listener")
    @Test
    void put_isRemovalListenerWorksCorrectExpireAfterSeconds_allEntriesWereRemoved() throws InterruptedException, ExecutionException {
        cache = lruCache.createCache(cacheMaximumSize, expireAfterSeconds);

        assertEquals(0, cache.size());

        cache.put(model1.getName(), model1);
        cache.put(model2.getName(), model2);
        cache.put(model3.getName(), model3);
        cache.put(model4.getName(), model4);

        Thread.sleep(2000);

        cache.get(model4.getName());

        assertTrue(memoryAppender.contains("Removed entry: name1  --> Model(name=name1)", Level.INFO));
        assertTrue(memoryAppender.contains("Removed entry: name2  --> Model(name=name2)", Level.INFO));
        assertTrue(memoryAppender.contains("Removed entry: name3  --> Model(name=name3)", Level.INFO));
        assertTrue(memoryAppender.contains("Removed entry: name4  --> Model(name=name4)", Level.INFO));
        assertTrue(memoryAppender.contains("Cause: SIZE", Level.INFO));
        assertTrue(memoryAppender.contains("Cause: EXPIRED", Level.INFO));

    }

    @DisplayName("Is LRU put() cache work correct for sequence of evict and expire, @TestFactory")
    @TestFactory
    Collection<DynamicTest> putEndToEnd_isCorrectWorksForSequenceOfEvictAndExpireTime_true() {
        cache = lruCache.createCache(cacheMaximumSize, expireAfterSeconds);

        return Arrays.asList(
                dynamicTest("assert cache size == 0", () -> assertEquals(cache.size(), 0)),

                dynamicTest("put 1st value", () -> cache.put(model1.getName(), model1)),
                dynamicTest("put 2nd value", () -> cache.put(model2.getName(), model2)),
                dynamicTest("put 3rd value", () -> cache.put(model3.getName(), model3)),

                dynamicTest("assert 1st exist in cache", () -> assertEquals(cache.get(model1.getName()), model1)),
                dynamicTest("assert 2st exist in cache", () -> assertEquals(cache.get(model2.getName()), model2)),
                dynamicTest("assert 3st exist in cache", () -> assertEquals(cache.get(model3.getName()), model3)),

                dynamicTest("assert cache size == 3", () -> assertEquals(cache.size(), 3)),

                dynamicTest("Sleep for 2 seconds", () -> Thread.sleep(2000)),

                dynamicTest("put 1st value", () -> cache.put(model1.getName(), model1)),
                dynamicTest("put 2nd value", () -> cache.put(model2.getName(), model2)),

                dynamicTest("get 1st value", () -> assertEquals(cache.get(model1.getName()), model1)),
                dynamicTest("get 2st value", () -> assertEquals(cache.get(model2.getName()), model2)),

                dynamicTest("assert cache size == 2", () -> assertEquals(cache.size(), 2)));

    }
}