package com.epam.ld.module2.testing.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.google.common.collect.ImmutableList;

import com.epam.ld.module2.testing.Messenger;
import com.epam.ld.module2.testing.MailServer;
import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Tests for MessageServiceImpl class")
@ExtendWith(MockitoExtension.class)
class MessageServiceImplTestIT {


    private final FileService fileService = new FileServiceImpl();
    private final MessageServiceImpl messageService = new MessageServiceImpl(fileService);

    private final Template template = new Template();
    private final MailServer mailServer = new MailServer();
    private final TemplateEngine templateEngine = new TemplateEngine(template);
    private final Messenger messenger = new Messenger(mailServer, templateEngine);

    private static final ImmutableList<String> simpleStringConsoleArgs = ImmutableList.of("name=dummy1"
            , "year=2023"
            , "address=dummy3");
    private static final ImmutableList<String> incorrectStringConsoleArgs = ImmutableList.of("name=1"
            , "year=ewq"
            , "address=sdfs");
    private static final ImmutableList<String> fullStringConsoleArgs = ImmutableList.of("name=Ann Mari"
            , "year=19.06.1986"
            , "address=Belarus Grodno street home flat");
    private static final ImmutableList<String> simpleStringValues = ImmutableList.of("dummy1",
            "2023", "dummy3");
    private static final ImmutableList<String> incorrectStringValues = ImmutableList.of("1",
            "ewq",
            "sdfs");
    private static final ImmutableList<String> fullStringData = ImmutableList.of("Ann Mari",
            "19.06.1986",
            "Belarus Grodno street home flat");


    @DisplayName("handleMessage() with consoleMode works and returns correct string, @ParameterizedTest")
    @ParameterizedTest
    @MethodSource("ArrayAndListProvider")
    void handleMessage_consoleModeReturnsCorrectString_worksCorrect(ImmutableList<String> args, List<String> list) {
        String actual = messageService.handleMessage(args, messenger);

        assertTrue(actual.contains(list.get(0)));
        assertTrue(actual.contains(list.get(1)));
        assertTrue(actual.contains(list.get(2)));
    }

    static Stream<Arguments> ArrayAndListProvider() {
        return Stream.of(
                arguments(simpleStringConsoleArgs, simpleStringValues),
                arguments(incorrectStringConsoleArgs, incorrectStringValues),
                arguments(fullStringConsoleArgs, fullStringData)
        );
    }

}