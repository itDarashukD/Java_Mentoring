package org.example.lfuCache;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.example.lfu.entity.Model;
import org.example.lfu.lfuCache.LfuCache;
import org.example.logAppender.MemoryAppender;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;


@DisplayName("Tests for LfuCache class")
@ExtendWith(MockitoExtension.class)
class LfuCacheTest {


    private static LfuCache cache;
    private static MemoryAppender memoryAppender;
    private static final Model model1 = new Model("name1");
    private static final Model model2 = new Model("name2");
    private static final Model model3 = new Model("name3");
    private static final Model model4 = new Model("name4");
    private static final Model model5 = new Model("name5");
    private static final Model model6 = new Model("name6");

    @BeforeAll
    static void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(LfuCache.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();

    }

    @DisplayName("Is get() work correct when Model not present in cache")
    @Test
    void get_returnNullWhenModelNotPresent_nullReturned() {
        cache = new LfuCache(2);

        Model model = cache.get(model1);
        assertNull(model);

    }

    @DisplayName("Is get() work correct when model present in cache")
    @Test
    void get_getReturnCorrectModelIfExistInCache_returnedEqualModel() {
        cache = new LfuCache(2);

        cache.put(model1);

        Model model = cache.get(model1);

        assertEquals(model, model1);

    }

    @DisplayName("Is put() work correct when capacity is incorrect")
    @Test
    void put_throwExceptionWhenCapacityIs0_exceptionThrew() {
        LfuCache badCache = new LfuCache(0);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> badCache.put(model1));

        assertEquals(ex.getMessage(), "The capacity must to be more than 0");

    }

    @DisplayName("Is put() work correct when cache contains Key")
    @Test
    void put_returnCorrectModelWhenCacheContainsKey_returnedEqualModel() {
        cache = new LfuCache(2);
        cache.put(model2);

        Model model = cache.put(model2);

        assertEquals(model, model2);

    }

    @DisplayName("Is put() work correct when cache is full")
    @Test
    void put_whenCacheSizeIsFullMustRemoveSomeValue_valueRemoved() {
        cache = new LfuCache(2);

        cache.put(model1);
        cache.put(model2);

        cache.put(model3);

        assertTrue(memoryAppender.contains("The name1 was removed from cache", Level.INFO));

    }

    @DisplayName("Is LFU cache work correct end to end test, @TestFactory")
    @TestFactory
    Collection<DynamicTest> lfuEndToEnd_isLFUCorrectWorksForSequenceOfPutGetMethods_true() {
        cache = new LfuCache(2);

        return Arrays.asList(
                dynamicTest("put 1st value", () -> assertEquals(cache.put(model1), model1)),
                dynamicTest("put 2nd value", () -> assertEquals(cache.put(model2), model2)),
                dynamicTest("put 3nd value", () -> assertEquals(cache.put(model3), model3)),

                dynamicTest("assert that 1st was removed", () -> assertNull(cache.get(model1))),

                dynamicTest("put 3nd value once again", () -> assertEquals(cache.put(model3), model3)),
                dynamicTest("put 4th value", () -> assertEquals(cache.put(model4), model4)),

                dynamicTest("assert that 2nd was removed", () -> assertNull(cache.get(model2))),

                dynamicTest("put 5th value", () -> assertEquals(cache.put(model5), model5)),

                dynamicTest("assert that 4st was removed", () -> assertNull(cache.get(model4))),
                dynamicTest("assert that 5th is present", () -> assertEquals(cache.get(model5), model5)),

                dynamicTest("put 6th value", () -> assertEquals(cache.put(model6), model6)),

                dynamicTest("assert that 3th was removed", () -> assertNull(cache.get(model3))));
    }

    @DisplayName("Is EvictCounter() work correct ")
    @Test
    void printEvictCounter_mustPrintCountOfEvictsWhenPutToFullCache_countPrinted() {
        cache = new LfuCache(2);

        cache.put(model1);
        cache.put(model2);

        cache.put(model3);
        cache.put(model4);
        cache.put(model5);

        cache.printEvictCounter();

        int correctEvictCounter = 3;
        assertTrue(memoryAppender.contains("The count of cache evictions is " + correctEvictCounter, Level.INFO));
    }
}