package com.builders.web;

import com.builders.utils.BuildersUtil;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HelloServletTest {

    @Test
    public void testHello() throws IOException {

        InputStream stream = BuildersUtil.class.getClassLoader().getResourceAsStream("web_config_test.properties");
        Properties properties = new Properties();

        properties.load(stream);

        assertNotNull(properties.getProperty("test_hello"));
    }

}
