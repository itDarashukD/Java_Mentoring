package com.epam.ld.module2.testing.service;

import java.io.File;
import java.util.Map;
import java.util.List;

public interface FileService {

    List<String> readFile(Map<String, String> fileNames);

    File writeToFile(String sentMessage, Map<String, String> fileNames);
}
