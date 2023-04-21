package com.example.concurrency.experiment;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        MySimpleMap<Integer, Integer> simpleMap = new MySimpleMap<>();
        simpleMap.put(1, 1);
        simpleMap.put(1, 2);
        simpleMap.put(2, 2);
        simpleMap.entrySet().stream().forEach(entry -> System.out.println(entry));

        System.out.println(simpleMap.get(1));
        System.out.println(simpleMap.get(2));

        MyThreadSafeMap<Integer, Integer> saveMap = new MyThreadSafeMap<>();
        saveMap.put(1, 1);
        saveMap.put(1, 2);
        saveMap.put(2, 2);
        saveMap.entrySet().stream().forEach(entry -> System.out.println(entry));

        System.out.println(saveMap.get(1));
        System.out.println(saveMap.get(2));
    }


}
