package org.example.server.exception;

import org.example.server.model.User;

public class InsufficientRightsException extends RuntimeException {

    private final static long serialVersionUID = 12345678901234L;

    public InsufficientRightsException(User user, String accessedPath) {
        super(String.format("User: %s don't have a access to accessedPath: %s",
	       user.getName(),
	       accessedPath));
    }

}
