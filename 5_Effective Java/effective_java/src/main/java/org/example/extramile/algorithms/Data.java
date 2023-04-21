package org.example.extramile.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Data {

    private final List<Integer> integers = new ArrayList<>();

    protected List<Integer> preparedUnsortedData() {
        fillUpList();
        return integers;
    }

    protected List<Integer> prepareSortedData() {
        fillUpList();
        List<Integer> sortIntegers = sortList();

        return sortIntegers;
    }

    private void fillUpList() {
        integers.add(1);
        integers.add(13);
        integers.add(53);
        integers.add(56);
        integers.add(41);
        integers.add(56);
        integers.add(0);
        integers.add(152);
        integers.add(45);
        integers.add(87);
        integers.add(99);
        integers.add(99);
        integers.add(-99);
        integers.add(-1);
    }

    private List<Integer> sortList() {
        return integers.stream().sorted().collect(Collectors.toList());
    }
}
