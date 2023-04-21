package org.example.server;

import java.util.List;
import java.util.NoSuchElementException;
import org.example.server.model.AccessLevelSettings;
import org.example.server.model.User;
import org.example.server.service.FileService;
import org.example.server.service.impl.FileServiceImpl;

public class AccessChecker {

    private static final String URLS_ACCESS_LEVEL_FILE_PATH = "files/usersUrlAccess/UrlsForAccessLevel.json";
    private static AccessChecker instance;
    private final ServerConfig config = ServerConfig.getInstance();
    private AccessLevelSettings accessLevelSettings;

    private AccessChecker() {
        FileService<AccessLevelSettings> fileService = new FileServiceImpl();
        accessLevelSettings = fileService.readSettings(URLS_ACCESS_LEVEL_FILE_PATH, AccessLevelSettings.class);
    }

    public static AccessChecker getInstance() {
        if (instance == null) {
	   return new AccessChecker();
        } else {
	   return instance;
        }
    }

    public boolean mayAccess(User user, String accessedPath) {
        AccessLevel accessLevel = config.getAccessLevel(user);

        List<String> paths = accessLevelSettings.getAccessForUrls().get(accessLevel);
        if (paths == null) {
	   throw new NoSuchElementException(String.format("Paths for accessLevel: %s not found",
		  accessLevel));
        }
        return paths.contains(accessedPath);
    }

}
