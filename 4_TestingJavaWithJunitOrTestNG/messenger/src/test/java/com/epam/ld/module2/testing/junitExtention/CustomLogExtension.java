package com.epam.ld.module2.testing.junitExtention;

import static java.nio.file.Files.deleteIfExists;

import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.epam.ld.module2.testing.service.FileServiceImpl;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.function.Function;


public class CustomLogExtension implements TestWatcher, AfterAllCallback {


    private static final String PATH_TO_FILE = "src/test/resources/testFiles/testResults/result.txt";
    private List<TestResultStatus> testResultsStatus = new ArrayList<>();
    private Path file;

    private static enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED;
    }

    @Override
    public void afterAll(ExtensionContext context) {
        Map<TestResultStatus, Long> summary = testResultsStatus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String displayName = context.getDisplayName();
        String format = String.format("Test result summary for %s %s", displayName, summary.toString());

        writeTestResultsToFile(format);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String format = String.format("Test Aborted for test %s: ", context.getDisplayName());

        writeTestResultsToFile(format);
        testResultsStatus.add(TestResultStatus.ABORTED);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String format = String.format("Test Failed for test %s: ", context.getDisplayName());
        writeTestResultsToFile(format);
        testResultsStatus.add(TestResultStatus.FAILED);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String format = String.format("Test Disabled for test %s: with reason :- %s",
                context.getDisplayName(),
                reason.orElse("No reason"));
        writeTestResultsToFile(format);
        testResultsStatus.add(TestResultStatus.DISABLED);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        String format = String.format("Test Successful for test %s: ", context.getDisplayName());
        writeTestResultsToFile(format);
        testResultsStatus.add(TestResultStatus.SUCCESSFUL);
    }

    private String writeTestResultsToFile(String format) {
        File file;

        deleteFileWithOldestResults();
        createClearFile();

        try {
            Path write = Files.write(Paths.get(PATH_TO_FILE), format.getBytes());
            file = write.toFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file.getName();
    }

    private void deleteFileWithOldestResults() {
        try {
            deleteIfExists(Path.of(PATH_TO_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createClearFile() {
        try {
            file = Files.createFile(Paths.get(PATH_TO_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}