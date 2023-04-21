package org.example.extramile.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;



@DisplayName("Tests for Search class")
@ExtendWith({MockitoExtension.class})
class SearchTest extends TestSupporter {

    @Mock
    private Data data;
    @Mock
    private BinarySearch binarySearch;
    @InjectMocks
    private Search search = new Search(data, binarySearch);

    private static final int dummyKey = 1;
    private static List<Integer> sortedIntegerList = prepareSortedData();


    @DisplayName("doBinarySearch() throw exception when List is empty")
    @Test
    void doBinarySearch_throwExceptionWhenListEmpty_exceptionThrew() {
        when(data.prepareSortedData()).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> search.doBinarySearch(dummyKey));

        assertEquals(ex.getMessage(), "Please check you List with values, seems that it empty");
    }

    @DisplayName("doBinarySearch() throw exception when key is null")
    @Test
    void doBinarySearch_throwExceptionWhenKeyIsNull_exceptionThrew() {
        when(data.prepareSortedData()).thenReturn(sortedIntegerList);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> search.doBinarySearch(null));

        assertEquals(ex.getMessage(), "The key must be present and must not be null");
    }

    @DisplayName("doBinarySearch() throw exception when List not contain key ")
    @Test
    void doBinarySearch_throwExceptionWhenListNotContainsKey_exceptionThrew() {
        when(data.prepareSortedData()).thenReturn(sortedIntegerList);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> search.doBinarySearch(999999999));

        assertEquals(ex.getMessage(), "The key must be present and must not be null");
    }

    @DisplayName("doSearch() is binary search do invoke search methods ")
    @Test
    void doSearch_isSearchMethodsInvoked_true() {
        when(data.prepareSortedData()).thenReturn(sortedIntegerList);
        when(binarySearch.doSearch(anyList(), any(Integer.class), anyInt(), anyInt())).thenReturn(1);

        search.doBinarySearch(0);

        verify(binarySearch, times(1)).doSearch(anyList(), any(Integer.class), anyInt(), anyInt());

    }
}