package org.example.server;

import java.util.NoSuchElementException;
import org.example.server.model.ServerSettings;
import org.example.server.model.User;
import org.example.server.service.FileService;
import org.example.server.service.impl.FileServiceImpl;

public class ServerConfig {

    private static final String SERVER_CONFIG_FILE_PATH = "files/usersUrlAccess/ServerConfiguration.json";
    private static ServerConfig instance;
    ServerSettings serverSettings;

    private ServerConfig() {
        FileService<ServerSettings> fileService = new FileServiceImpl();
        serverSettings = fileService.readSettings(SERVER_CONFIG_FILE_PATH, ServerSettings.class);
    }


    public static ServerConfig getInstance() {
        if (instance == null) {
	   return new ServerConfig();
        } else {
	   return instance;
        }
    }

    public AccessLevel getAccessLevel(User user) {
        AccessLevel accessLevel = serverSettings.getUserAccessSettings().get(user.getName());
        if (accessLevel == null) {
	   throw new NoSuchElementException(String.format("Access level for user: %s not found",
		  user.getName()));
        }
        return accessLevel;
    }

}
