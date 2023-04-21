package com.builders.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class BuildersUtilTest {

    @Test
    public void isResultSayHelloFromUtilsCorrect() {
        String expected = "Hello from utils!";

        String actual = new BuildersUtil().sayHello();

        assertEquals(actual,expected);
    }

}
