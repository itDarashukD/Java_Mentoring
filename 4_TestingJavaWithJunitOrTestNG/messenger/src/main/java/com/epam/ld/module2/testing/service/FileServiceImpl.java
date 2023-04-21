package com.epam.ld.module2.testing.service;


import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class FileServiceImpl implements FileService {

    private static final Logger log = Logger.getLogger(FileServiceImpl.class.getName());
    private final String PATH_TO_FILES_FOLDER = "src/main/resources/";

    @Override
    public List<String> readFile(Map<String, String> fileNames) {
        String pathToInputFile = PATH_TO_FILES_FOLDER + fileNames.get("contextFile");
        List<String> context = null;

        log.info("IN readFile() - start reading file : " + pathToInputFile);

        try {
            Path path = Paths.get(pathToInputFile);
            context = Files.lines(path)
                    .map(String::trim)
                    .map(line -> line.split(" "))
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            return context;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File writeToFile(String sentMessage, Map<String, String> fileNames) {
        String pathToSentFile = PATH_TO_FILES_FOLDER + fileNames.get("resultFile");
        File file;

        log.info("IN writeToFile() - start writing to file : " + pathToSentFile);

        try {
            Path path = Paths.get(pathToSentFile);
            Path write = Files.write(path, sentMessage.getBytes());
            file = write.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

}


