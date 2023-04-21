package org.example.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.example.server.model.Settings;
import org.example.server.service.FileService;
import org.springframework.util.ResourceUtils;

public class FileServiceImpl<T extends Settings> implements FileService<T> {

    @Override
    public T readSettings(String filePath, Class<T> targetClass) {
        ObjectMapper mapper = new ObjectMapper();
        T settings;

        File downloadedFile = downloadFile(filePath);

        try {
	   settings = mapper.readValue(downloadedFile, targetClass);
        } catch (IOException e) {
	   throw new RuntimeException(e);
        }
        return settings;
    }


    private File downloadFile(String filePath) {
        File file;
        try {
	   file = ResourceUtils.getFile("classpath:" + filePath);
        } catch (IOException e) {
	   throw new RuntimeException(e);
        }
        return file;
    }

}