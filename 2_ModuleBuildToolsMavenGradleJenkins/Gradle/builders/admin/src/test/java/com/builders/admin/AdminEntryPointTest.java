package com.builders.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AdminEntryPointTest {

    @Test
    public void isResultGreetingCorrect() {
        String[] args = new String[]{"nice", "to", "see", "you"};

        String actual = new AdminEntryPoint().sayHello(args);

        assertTrue(actual.contains("nice, to, see, you"));
        assertTrue(actual.contains("I say Hello from admin"));
        assertTrue(actual.contains("Hello from services"));
        assertTrue(actual.contains("Hello from utils"));
    }

}
