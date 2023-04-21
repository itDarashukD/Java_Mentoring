package com.epam.ld.module2.testing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Tests for FileServiceImpl class")
@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    private static final String SOME_MESSAGE = "some message";
    private static final String PATH_TO_TEST_FILES_FOLDER = "src/test/resources/testFiles/";
    private static final Path
	   pathToKeyValueFile =
	   Paths.get(PATH_TO_TEST_FILES_FOLDER + "letterContextFileTest.txt");
    private static final Path
	   pathToEmptyFile =
	   Paths.get(PATH_TO_TEST_FILES_FOLDER + "letterContextFile_Empty.txt");
    private static final Path
	   pathThreeSimpleStringValuesFile =
	   Paths.get(PATH_TO_TEST_FILES_FOLDER + "letterContextFile_threeSimpleStringValues.txt");
    private static final Path
	   pathToDigitFile =
	   Paths.get(PATH_TO_TEST_FILES_FOLDER + "letterContextFile_digits.txt");
    private static final Path
	   pathNotExistFile =
	   Paths.get(PATH_TO_TEST_FILES_FOLDER + "notExistFile.txt");
    private static final Map<String, String> mapWithContextFileName = new HashMap<>() {{
        put("contextFile", "dummy1.txt");
        put("resultFile", "dummy2.txt");
    }};
    private final static List<String> correctReadingResultKeyValue = new ArrayList<>() {{
        add("name=NameFromFile");
        add("year=2023");
        add("address=Bialystok");
    }};
    private static final List<String> correctReadingResultSimpleStrings = new ArrayList<>() {{
        add("NameFromFile");
        add("2023");
        add("Bialystok");
    }};
    private static final List<String> correctReadingResultEmptyFile = new ArrayList<>();
    private static final List<String> correctReadingResultDigits = new ArrayList<>() {{
        add("43");
        add("42");
        add("3");
        add("432");
        add("43");
        add("23");
    }};
    @TempDir(cleanup = CleanupMode.ON_SUCCESS)
    static Path tempDirectory;
    @InjectMocks
    private FileServiceImpl fileService;

    static Stream<Arguments> pathAndListProvider() {
        return Stream.of(arguments(pathToKeyValueFile, correctReadingResultKeyValue),
	       arguments(pathThreeSimpleStringValuesFile, correctReadingResultSimpleStrings),
	       arguments(pathToEmptyFile, correctReadingResultEmptyFile),
	       arguments(pathToDigitFile, correctReadingResultDigits));
    }

    @DisplayName("readFile() returns correct String after readings the files")
    @ParameterizedTest
    @MethodSource("pathAndListProvider")
    void readFile_returnCorrectValue_returnedWholeContentFromFile(Path path, ArrayList<String> correctReadingResult) {
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(path);

	   List<String> actual = fileService.readFile(mapWithContextFileName);

	   assertEquals(actual, correctReadingResult);
        }
    }

    @Test()
    @DisplayName("readFile() throws RuntimeException when file is not exist")
    void readFile_whenFileNotExist_exceptionThrown() {
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(pathNotExistFile);

	   assertThrows(RuntimeException.class,
		  () -> fileService.readFile((mapWithContextFileName)));
        }
    }

    @DisplayName("writeFile() do correct writing to file")
    @Test
    void writeToFile_writingToFile_wroteFileExistAndIncludeContent() {
        Path resolve = tempDirectory.resolve("test.txt");
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {

	   dummy.when(() -> Paths.get(anyString())).thenReturn(resolve);

	   File actualFile = fileService.writeToFile(SOME_MESSAGE, mapWithContextFileName);

	   assertTrue(actualFile.exists());
	   assertTrue(actualFile.length() > 0);
	   assertEquals("test.txt", actualFile.getName());
        }
    }

    @Test()
    @DisplayName("writeToFile() throws Null pointer exception when sentFile is null")
    void writeToFile_whenSentMessageIsNull_exceptionThrown() {
        Path resolve = tempDirectory.resolve("test.txt");
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(resolve);

	   assertThrows(NullPointerException.class,
		  () -> fileService.writeToFile(null, mapWithContextFileName));
        }
    }

}