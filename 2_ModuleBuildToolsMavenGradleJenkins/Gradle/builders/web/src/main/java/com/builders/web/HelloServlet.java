package com.builders.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.builders.services.BuilderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class HelloServlet extends HttpServlet {

    private static final String HELLO_PROPERTY ="web_config.properties";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String hello = new BuilderService().sayHello();
        InputStream stream = HelloServlet.class.getClassLoader().getResourceAsStream(HELLO_PROPERTY);
        Properties properties = new Properties();

        try {
            properties.load(stream);

            String helloFromProperty = properties.getProperty("hello");
            String resultHelloString = String.format("%s %s ", hello, helloFromProperty);

            req.setAttribute("hello", resultHelloString);
            req.getRequestDispatcher("hello.jsp").forward(req, resp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
