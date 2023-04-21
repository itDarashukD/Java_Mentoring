package com.builders.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BuildersUtil {

    private static final String HELLO_PROPERTY ="utils_config.properties";

    public String sayHello() {
        String hello = "";
        InputStream stream = BuildersUtil.class.getClassLoader().getResourceAsStream(HELLO_PROPERTY);
        Properties properties = new Properties();
        try {
            properties.load(stream);

            hello = properties.getProperty("hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hello;
    }

}
