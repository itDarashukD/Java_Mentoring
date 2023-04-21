package com.example.concurrency.experiment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Fork(1)
@Threads(10)
@Warmup(iterations = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MySimpleMapTest {


    @Benchmark
    public void randomReadAndWriteSimpleMap() {
        MySimpleMap<Integer, Integer> simpleMap = new MySimpleMap<>();
        performReadAndWriteTest(simpleMap);
    }

    @Benchmark
    public void randomReadSimpleMap() {
        MySimpleMap<Integer, Integer> simpleMap = new MySimpleMap<>();
        performReadAndWriteTest(simpleMap);
    }

    @Benchmark
    public void randomWriteSimpleMap() {
        MySimpleMap<Integer, Integer> simpleMap = new MySimpleMap<>();
        performWriteTest(simpleMap);
    }

    @Benchmark
    public void randomReadAndWriteThreadSafeMap() {
        MyThreadSafeMap<Integer, Integer> safeMap = new MyThreadSafeMap<>();
        performReadAndWriteTest(safeMap);
    }

    @Benchmark
    public void randomWriteThreadSafeMap() {
        MyThreadSafeMap<Integer, Integer> safeMap = new MyThreadSafeMap<>();
        performWriteTest(safeMap);
    }

    @Benchmark
    public void randomReadThreadSafeMap() {
        MyThreadSafeMap<Integer, Integer> safeMap = new MyThreadSafeMap<>();
        performReadTest(safeMap);
    }


    @Benchmark
    public void randomReadAndWriteConcurrentHashMap() {
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        performReadAndWriteTest(concurrentHashMap);
    }

    @Benchmark
    public void randomWriteConcurrentHashMap() {
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        performWriteTest(concurrentHashMap);
    }

    @Benchmark
    public void randomReadConcurrentHashMap() {
        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        performReadTest(concurrentHashMap);
    }


    private void performReadAndWriteTest(final Map<Integer, Integer> map) {
        for (int i = 0; i < 1000; i++) {
	   Integer randNumber = (int) Math.ceil(Math.random() * 1000);
	   map.get(randNumber);
	   map.put(randNumber, randNumber);
        }
    }

    private void performReadTest(final Map<Integer, Integer> map) {
        for (int i = 0; i < 1000; i++) {
	   Integer randNumber = (int) Math.ceil(Math.random() * 1000);
	   map.get(randNumber);
        }
    }

    private void performWriteTest(final Map<Integer, Integer> map) {
        for (int i = 0; i < 1000; i++) {
	   Integer randNumber = (int) Math.ceil(Math.random() * 1000);
	   map.put(randNumber, randNumber);
        }
    }


}