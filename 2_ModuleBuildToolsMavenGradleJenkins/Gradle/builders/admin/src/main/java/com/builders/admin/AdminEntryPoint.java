package com.builders.admin;

import com.builders.services.BuilderService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class AdminEntryPoint {

    private static final String HELLO_PROPERTY ="admin_config.properties";

    public static void main(String[] args) {
        String resultGreetings = new AdminEntryPoint().sayHello(args);
        System.out.println(resultGreetings);
    }
        public String sayHello (String[] args) {
            String format="";
            String helloFromService = new BuilderService().sayHello();
            InputStream stream = AdminEntryPoint.class.getClassLoader().getResourceAsStream(HELLO_PROPERTY);
            Properties properties = new Properties();

            try {
                properties.load(stream);

                String hello = properties.getProperty("hello");
                String inputHello = Arrays.deepToString(args);
                format = String.format("You say %s , I say %s %s", inputHello, hello, helloFromService);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return format;
        }
    }
