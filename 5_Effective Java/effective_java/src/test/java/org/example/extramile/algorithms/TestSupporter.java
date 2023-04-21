package org.example.extramile.algorithms;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class TestSupporter {

    public static List<Integer> prepareRawIntegersList() {
        Integer[] ArrayIntegers = {1, 13, 53, 56, 41, 56, 0, 152, 45, 87, 99, 99, -1, -99};

        return new ArrayList<>(List.of(ArrayIntegers));
    }

    public static List<Integer> prepareSortedData() {
        List<Integer> rawIntegerList = prepareRawIntegersList();
        List<Integer> sortedIntegers = rawIntegerList.stream().sorted().collect(Collectors.toList());

        return sortedIntegers;
    }

}
