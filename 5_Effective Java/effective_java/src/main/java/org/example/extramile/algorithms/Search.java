package org.example.extramile.algorithms;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Search {

    private static final Logger log = (Logger) LoggerFactory.getLogger(Search.class);

    private Data data;
    private BinarySearch binarySearch;

    public Search(Data data, BinarySearch binarySearch) {
        this.data = data;
        this.binarySearch = binarySearch;
    }

    public int doBinarySearch(Integer key) {
        List<Integer> integers = data.prepareSortedData();
        log.info("The sorted list : {}", integers);

        if (integers.isEmpty()) {
            throw new IllegalArgumentException("Please check you List with values, seems that it empty");
        }

        if (key == null || !integers.contains(key)) {
            throw new IllegalArgumentException("The key must be present and must not be null");
        }

        int lowIndex = 0;
        int highIndex = integers.size() - 1;

        return binarySearch.doSearch(integers, key, lowIndex, highIndex);
    }
}
