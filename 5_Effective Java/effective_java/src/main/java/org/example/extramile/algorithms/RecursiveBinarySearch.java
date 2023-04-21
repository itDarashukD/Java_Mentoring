package org.example.extramile.algorithms;

import java.util.List;

public class RecursiveBinarySearch  implements BinarySearch {


    @Override
    public int doSearch(List<Integer> integers, Integer key, int lowIndex, int highIndex) {
        int targetIndex = 0;

        int middle = lowIndex + ((highIndex - lowIndex) / 2);

        if (integers.get(middle) > key) {
            highIndex = middle - 1;
            return doSearch(integers, key, lowIndex, highIndex);
        }

        if (integers.get(middle) < key) {
            lowIndex = middle + 1;
            return doSearch(integers, key, lowIndex, highIndex);
        }

        if (integers.get(middle).equals(key)) {
            targetIndex = key;
        }

        return targetIndex;
    }

}
