package org.example.extramile.algorithms;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class MergeSort {

    private static final Logger log = (Logger) LoggerFactory.getLogger(IterativeBinarySearch.class);

    private Data data;

    public MergeSort(Data data) {
        this.data = data;
    }

    public List<Integer> sorting() {
        List<Integer> integers = data.preparedUnsortedData();
        log.info("The sorted list : {}", integers);

        if (integers.isEmpty()) {
            throw new IllegalArgumentException("Please check you List with values, seems that it empty");
        }

        int lowIndex = 0;
        int highIndex = integers.size() - 1;

        return sorting(integers, lowIndex, highIndex);
    }

    public List<Integer> sorting(List<Integer> integers, int lowIndex, int highIndex) {
        List<Integer> list = new ArrayList<>();

        if (lowIndex == highIndex) {
            Integer lonelyValue = integers.get(lowIndex);
            list.add(lonelyValue);
            return list;
        }

        int middleIndex = (lowIndex + highIndex) / 2;

        List<Integer> leftList = sorting(integers, lowIndex, middleIndex);
        List<Integer> rightList = sorting(integers, middleIndex + 1, highIndex);

        return mergeSorting(leftList, rightList);
    }

    private List<Integer> mergeSorting(List<Integer> leftList, List<Integer> rightList) {
        List<Integer> sorted = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < leftList.size() && j < rightList.size()) {

            if (leftList.get(i) < rightList.get(j)) {
                sorted.add(leftList.get(i));
                i++;
            } else {
                sorted.add(rightList.get(j));
                j++;
            }
        }

        if (i == leftList.size()) {

            while (j < rightList.size()) {
                sorted.add(rightList.get(j));
                j++;
            }
        }

        if (j == rightList.size()) {

            while (i < leftList.size()) {
                sorted.add(leftList.get(i));
                i++;
            }
        }
        return sorted;
    }

}
