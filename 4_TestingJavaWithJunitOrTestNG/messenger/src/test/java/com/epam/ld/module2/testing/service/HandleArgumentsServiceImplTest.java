package com.epam.ld.module2.testing.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.ImmutableList;


@DisplayName("Tests for HandleArgumentsServiceImplTest class")
@ExtendWith(MockitoExtension.class)
class HandleArgumentsServiceImplTest {

    @InjectMocks
    HandleArgumentsServiceImpl handleArgumentService;

    private static final String[] emptyArguments = {};
    private static final String[] threeStringArguments = {"--file", "contextFile=letterContextFile.txt", "resultFile=sentLetterFile.txt"};


    @Test()
    @DisplayName("throws IllegalArgumentException when bad argument in handleArgumentService()")
    void handleMessage_whenArgumentIsBlank_exceptionThrown() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> handleArgumentService.handleArrayArguments(emptyArguments));

        assertEquals(ex.getMessage(), "IN handleArrayArguments() -  arguments are absent!)");
    }


    @Test
    @DisplayName("handle arguments from array , return ImmutableList")
    void handleArrayArguments_handleThreeArgumentsFromArray_argumentPresentInNewImmutableList() {

        ImmutableList<String> strings = handleArgumentService.handleArrayArguments(threeStringArguments);

        assertEquals(strings.size(), 3);
        assertTrue(strings.contains("--file"));
        assertTrue(strings.contains("resultFile=sentLetterFile.txt"));
        assertTrue(strings.contains("contextFile=letterContextFile.txt"));
    }
}