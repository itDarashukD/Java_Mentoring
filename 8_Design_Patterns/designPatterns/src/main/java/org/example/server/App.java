package org.example.server;

import org.example.server.model.Session;
import org.example.server.model.User;

public class App {

    public static void main(String[] args) {
        SessionManager sessionManager = new SessionManager();
        Session session = sessionManager.createSession(new User("Dzmitry"), "http://admin/and/developer/access.com");
        System.out.println(session);
    }

}
