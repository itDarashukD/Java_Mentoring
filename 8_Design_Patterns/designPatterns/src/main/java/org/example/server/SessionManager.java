package org.example.server;

import org.example.server.exception.InsufficientRightsException;
import org.example.server.model.Session;
import org.example.server.model.User;

/*The following code deals with the creation of new user sessions
 * Your job is to write unit tests for the createSession method.*/

public class SessionManager {

    private AccessChecker accessChecker = AccessChecker.getInstance();

    public Session createSession(User user, String accessedPath) {
        if (accessChecker.mayAccess(user, accessedPath)) {
	   return new Session(user);
        } else {
	   throw new InsufficientRightsException(user, accessedPath);
        }
    }

}
