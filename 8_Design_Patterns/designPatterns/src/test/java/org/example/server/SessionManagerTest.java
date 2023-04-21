package org.example.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.example.server.exception.InsufficientRightsException;
import org.example.server.model.Session;
import org.example.server.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("tests for SessionManager class")
@ExtendWith(MockitoExtension.class)
class SessionManagerTest {

    @InjectMocks
    private SessionManager sessionManager;
    @Mock
    private AccessChecker accessChecker;

    private static final User userWithName111 = new User("dummyName111");
    private static final String dummyAccessedPath = "dummyAccessedPath";


    @DisplayName("createSession(), check if new Session created when access granted")
    @Test
    void createSession_accessGranted_sessionReturned() {
        when(accessChecker.mayAccess(any(User.class), anyString())).thenReturn(true);

        Session session = sessionManager.createSession(userWithName111, dummyAccessedPath);

        assertNotNull(session);
    }

    @DisplayName("createSession(), check if exception thrown when access not granted")
    @Test
    void createSession_accessNotGranted_exceptionThrown() {
        when(accessChecker.mayAccess(any(User.class), anyString())).thenReturn(false);

        Exception exception = assertThrows(InsufficientRightsException.class,
	       () -> sessionManager.createSession(userWithName111, dummyAccessedPath));

        assertEquals(exception.getMessage(),
	       String.format("The user: %s don't have a access to accessedPath: %s",
		      userWithName111.getName(),
		      dummyAccessedPath));
    }

}