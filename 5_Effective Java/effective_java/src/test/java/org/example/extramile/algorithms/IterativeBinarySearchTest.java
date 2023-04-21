package org.example.extramile.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


@DisplayName("Tests for IterativeBinarySearch class")
@State(Scope.Benchmark)
@ExtendWith(MockitoExtension.class)
public class IterativeBinarySearchTest extends TestSupporter {

    @InjectMocks
    private IterativeBinarySearch search;

    private final static int lowIndex = 0;
    private static List<Integer> sortedIntegerList = prepareSortedData();
    private static int highIndex = sortedIntegerList.size() - 1;


    @DisplayName("Is iterative search works correct ")
    @TestFactory
    public Collection<DynamicTest> doSearch_isIterativeSearchReturnsCorrectValues_allReturnedValuesAreCorrect() {
        return Arrays.asList(
                dynamicTest("find value from provided list", () -> assertEquals(search.doSearch(sortedIntegerList, -99, lowIndex, highIndex), -99)),
                dynamicTest("find negative number from provided list", () -> assertEquals(search.doSearch(sortedIntegerList, -1, lowIndex, highIndex), -1)),
                dynamicTest("find zero from provided list", () -> assertEquals(search.doSearch(sortedIntegerList, 0, lowIndex, highIndex), 0)),
                dynamicTest("find random value from provided list", () -> assertEquals(search.doSearch(sortedIntegerList, 99, lowIndex, highIndex), 99)),
                dynamicTest("find middle value from provided list", () -> assertEquals(search.doSearch(sortedIntegerList, 53, lowIndex, highIndex), 53)),
                dynamicTest("find highest value from provided list", () -> assertEquals(search.doSearch(sortedIntegerList, 152, lowIndex, highIndex), 152)));

    }

    @Test
    public void
    launchBenchmark() throws Exception {
        Options options = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .threads(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(options).run();
    }


    @Benchmark
    @Fork(value = 3, warmups = 0)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 3, time = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @DisplayName("The Benchmark test for Iterative search method with extremal values ")
    @TestFactory
    public Collection<DynamicTest> doSearch_benchmarkMeasurementsForIterativeForExtremalValues_benchmarksWereGot() {
        return Arrays.asList(
                dynamicTest("test minimal value from provided list", () -> search.doSearch(sortedIntegerList, -99, 0, sortedIntegerList.size() - 1)),
                dynamicTest("find middle value from provided list", () -> search.doSearch(sortedIntegerList, 53, 0, sortedIntegerList.size() - 1)),
                dynamicTest("find highest value from provided list", () -> search.doSearch(sortedIntegerList, 152, 0, sortedIntegerList.size() - 1)));

    }
}