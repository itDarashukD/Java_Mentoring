package com.epam.ld.module2.testing.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.epam.ld.module2.testing.MailServer;
import com.epam.ld.module2.testing.Messenger;
import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import com.epam.ld.module2.testing.logAppender.MemoryAppender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Tests for MessageServiceImpl class")
@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @InjectMocks
    private MessageServiceImpl messageService;
    @Mock
    private FileService fileService;
    @TempDir(cleanup = CleanupMode.ON_SUCCESS)
    static Path tempDirectory;

    private final Template template = new Template();
    private final MailServer mailServer = new MailServer();
    private final TemplateEngine templateEngine = new TemplateEngine(template);
    private final Messenger messenger = new Messenger(mailServer, templateEngine);

    private static MemoryAppender memoryAppender;
    private static final String sentMessage =
            "The message to address: ' Bialystok '  ,and content : ' Dear msr. dummy1, Best wishes in terms of 2023 New Year ! ' ,was sent.";
    private static final ImmutableList<String> notEnoughArgs = ImmutableList.of("--file");
    private static final ImmutableList<String> dummyArgsForFileMode =
            ImmutableList.of("--file", "dummy1=dummy1.txt", "dummy2=dummy2.txt");
    private static final ImmutableList<String> realArgsForFileMode =
            ImmutableList.of("--file", "contextFile=letterContextFile.txt", "resultFile=sentLetterFile.txt");
    private static final ImmutableList<String> argsForConsoleMode =
            ImmutableList.of("name=dummy1", "year=2023", "address=dummy3");
    private static final ImmutableList<String> firstArgIsBlankFileMode =
            ImmutableList.of("--file", " =dummy1.txt", "dummy2=dummy2.txt");
    private static final ImmutableList<String> secondArgIsBlankForFileMode =
            ImmutableList.of("--file", "dummy1=dummy1.txt", "dummy2= ");
    private static final Map<String, String> fileNames = new HashMap<>() {{
        put("dummy1", "dummy1.txt");
        put("dummy2", "dummy2.txt");
    }};
    private static final List<String> readFromFileValues = new ArrayList<>() {{
        add("name=dummy1");
        add("year=2023");
        add("address=Bialystok");
    }};
    private static final List<String> rawContextCorrectArgs = new ArrayList<>() {{
        add("name=dummy1");
        add("year=2023");
        add("address=Bialystok");
    }};


    @BeforeAll
    static void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(MessageServiceImpl.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();

    }

    @Test()
    @DisplayName("throws IllegalArgumentException when bad argument in handleFileOutput()")
    void handleMessage_whenNotEnoughArguments_exceptionThrown() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> messageService.handleMessage(notEnoughArgs, messenger));

        assertEquals(ex.getMessage(), "IN handleFileOutput() - not enough arguments!)");
    }

    @Test
    @DisplayName("handleMessage(), when arguments for fileMode - works correct, readFile() invoked")
    void handleMessage_whenArgsForRunFileMode_readFileMethodWasInvoked() {
        when(fileService.readFile(anyMap())).thenReturn(rawContextCorrectArgs);

        messageService.handleMessage(dummyArgsForFileMode, messenger);

        verify(fileService, times(1)).readFile(fileNames);
    }

    @Test()
    @DisplayName("handleMessage() throws IllegalArgumentException when some one of the argument is incorrect")
    void handleMessage_whenFirstArgumentIsBlank_exceptionThrown() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> messageService.handleMessage(firstArgIsBlankFileMode, messenger));

        assertEquals(ex.getMessage(), "IN prepareContext() - some of the arguments in context is blank!)");
    }

    @Test()
    @DisplayName("handleMessage() throws IllegalArgumentException when some one of the argument is bad")
    void handleMessage_whenSecondArgumentIsBlank_exceptionThrown() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> messageService.handleMessage(secondArgIsBlankForFileMode, messenger));

        assertEquals(ex.getMessage(), "IN prepareContext() - some of the arguments in context is blank!)");
    }

    @Test
    @DisplayName("handleFileOutput() is writeToFile() invoked ")
    void handleMessage_writeFileInvokedWhenFileMode_invoked() {
        Path resolve = tempDirectory.resolve("test.txt");

        when(fileService.readFile(anyMap())).thenReturn(rawContextCorrectArgs);
        when(fileService.writeToFile(anyString(), anyMap())).thenReturn(resolve.toFile());

        messageService.handleMessage(dummyArgsForFileMode, messenger);

        verify(fileService, times(1)).writeToFile(sentMessage, fileNames);
    }

    @Test
    @DisplayName("handleFileOutput() returned correct sent message")
    void handleMessage_returnCorrectSentMessage_returnedEqualSentMessage() {
        Path resolve = tempDirectory.resolve("test.txt");

        when(fileService.readFile(anyMap())).thenReturn(rawContextCorrectArgs);
        when(fileService.writeToFile(anyString(), anyMap())).thenReturn(resolve.toFile());

        String actual = messageService.handleMessage(dummyArgsForFileMode, messenger);

        assertEquals(actual, sentMessage);
    }

    @Test
    @DisplayName("isConsoleMode() invoked ")
    void handleMessage_isConsoleModeInvoked_true() {
        messageService.handleMessage(argsForConsoleMode, messenger);

        assertTrue(memoryAppender.contains("IN handleConsoleOutput() - start working ...", Level.INFO));
    }


    @Test
    @DisplayName("handleFileOutput() returned correct String, end to end test")
    void handleFileOutput_returnCorrectString_returnedEqualString_endToEndTest() {
        when(fileService.readFile(anyMap())).thenReturn(readFromFileValues);

        String actual = messageService.handleMessage(realArgsForFileMode, messenger);

        assertEquals(actual, sentMessage);
    }

}