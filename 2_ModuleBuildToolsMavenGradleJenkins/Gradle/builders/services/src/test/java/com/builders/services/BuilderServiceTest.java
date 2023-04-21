package com.builders.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BuilderServiceTest {

    @Test
    public void isResourcesLoadedCorrectly() {
        String expected = "Hello from services! Hello from utils!";

        String actual = new BuilderService().sayHello();

        assertEquals(expected, actual);
    }
}
