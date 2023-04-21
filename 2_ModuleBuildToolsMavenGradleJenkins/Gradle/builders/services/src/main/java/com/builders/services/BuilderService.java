package com.builders.services;

import com.builders.utils.BuildersUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BuilderService {

    private static final String HELLO_PROPERTY ="service_config.properties";

    public String sayHello() {
        String resultString = "";

        InputStream stream = BuilderService.class.getClassLoader().getResourceAsStream(HELLO_PROPERTY);
        Properties properties = new Properties();
        try {
            properties.load(stream);

            String hello = properties.getProperty("hello");
            String helloFromUtils = new BuildersUtil().sayHello();
            resultString = String.format("%s %s", hello, helloFromUtils);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;
    }

}
