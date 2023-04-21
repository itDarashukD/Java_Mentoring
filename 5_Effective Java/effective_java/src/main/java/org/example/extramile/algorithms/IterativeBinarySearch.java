package org.example.extramile.algorithms;

import java.util.List;


public class IterativeBinarySearch implements BinarySearch {

    @Override
    public int doSearch(List<Integer> integers, Integer key, int lowIndex, int highIndex) {
        int targetIndex = 0;

        while (highIndex >= lowIndex) {
            int middle = lowIndex + ((highIndex - lowIndex) / 2);
            if (integers.get(middle) < key) {
                lowIndex = middle + 1;
            }
            if (integers.get(middle) > key) {
                highIndex = middle - 1;
            }
            if (key.equals(integers.get(middle))) {
                targetIndex = key;
                break;
            }
        }
        return targetIndex;
    }

}
