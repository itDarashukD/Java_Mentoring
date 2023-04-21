package org.example.extramile.algorithms;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


@DisplayName("Tests for MergeSort class")
@State(Scope.Benchmark)
@ExtendWith(MockitoExtension.class)
public
class MergeSortTest extends TestSupporter {

    @Mock
    private Data data;

    @InjectMocks
    private MergeSort mergeSort = new MergeSort(data);

    private static int lowIndex = 0;
    static private List<Integer> sortedIntegerList = prepareSortedData();
    private static int highIndex = sortedIntegerList.size() - 1;
    static private List<Integer> integers = prepareRawIntegersList();


    @DisplayName("preparedUnsortedData(), is throw exception when List is empty")
    @Test
    void preparedUnsortedData_throwExceptionWhenListEmpty_exceptionThrow() {
        when(data.preparedUnsortedData()).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> mergeSort.sorting());

        assertEquals(ex.getMessage(), "Please check you List with values, seems that it empty");
    }

    @DisplayName("sorting(), is merge sorting works correct ,returning all values")
    @Test
    void sorting_mergeSortingReturnsCorrectValues_expectedValuesAreCorrect() {
        List<Integer> actual = mergeSort.sorting(integers, lowIndex, highIndex);

        assertEquals(actual.size(), integers.size());
        assertEquals(actual, sortedIntegerList);
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
    @DisplayName("sorting(), is merge sorting correct ")
    @Test
    public void sorting_benchmarkMeasurementsForMergeSorting_benchmarksWereGot() {
        List<Integer> unsortedList = prepareRawIntegersList();
        lowIndex = 0;
        highIndex = unsortedList.size() - 1;

        mergeSort.sorting(unsortedList, lowIndex, highIndex);
    }
}