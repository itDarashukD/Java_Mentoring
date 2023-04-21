package com.epam.ld.module2.testing.template;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("Tests for TemplateEngine class")
@ExtendWith(MockitoExtension.class)
class TemplateEngineTest {

    @Mock
    private Template template;
    @InjectMocks
    private TemplateEngine templateEngine = new TemplateEngine(template);

    private static final String EMPTY_TEMPLATE = "";
    private static final String CORRECT_TEMPLATE =
            "Dear msr. #{name}, Best wishes in terms of #{year} New Year !";
    private static final String EXPECTED_RESULT_WITH_DIGITS =
            "Dear msr. dummy2, Best wishes in terms of 0 New Year !";
    private static final String EXPECTED_RESULT_WITH_CORRECT_CONTEXT_VALUES =
            "Dear msr. Dzmitry, Best wishes in terms of 2023 New Year !";
    private static final String EXPECTED_RESULT_WITH_CORRECT_CONTEXT_DUMMY_VALUES =
            "Dear msr. dummy1, Best wishes in terms of 11111111 New Year !";
    private static final String EXCEPTION_MESSAGE =
            "IN generateMessage() - some values in context are missing!)";
    private static final List<String> templateValues = new ArrayList<>() {{
        add("name");
        add("year");
    }};
    private static final HashMap<String, String> incorrectContextOnlyYearArgument = new HashMap<>() {{
        put("year", "dummyYear");
    }};
    private static final HashMap<String, String> incorrectContextOnlyNameArgument = new HashMap<>() {{
        put("name", "dummyName");
    }};
    private static final HashMap<String, String> incorrectContextNoActualArguments = new HashMap<>() {{
        put("some dummy key1", "dummyName");
        put("some dummy key2", "dummyName");
    }};
    private static final HashMap<String, String> emptyContext = new HashMap<>();
    private static final HashMap<String, String> correctContextRealValues = new HashMap<>() {{
        put("year", "2023");
        put("name", "Dzmitry");
    }};
    private static final HashMap<String, String> correctContextDummyValues = new HashMap<>() {{
        put("name", "dummy1");
        put("year", "11111111");
    }};
    private static final HashMap<String, String> correctContextWithDigits = new HashMap<>() {{
        put("year", "0");
        put("name", "dummy2");
    }};


    @Test()
    @DisplayName("generateMessage() throws IllegalArgumentException when emailTemplate is blank ")
    void generateMessage_emailTemplateIsBlank_exceptionThrown() {
        when(template.getEmailTemplate()).thenReturn(EMPTY_TEMPLATE);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> templateEngine.generateMessage(correctContextRealValues));

        assertEquals(ex.getMessage(), "IN generateMessage() - emailTemplate is Blank!)");
    }


    @DisplayName("generateMessage() throws IllegalArgumentException when some values in context are missing, @ParameterizedTest")
    @ParameterizedTest
    @MethodSource("mapProvider")
    void generateMessage_someValuesInContextAreAbsent_exceptionThrown(HashMap<String, String> incorrectContext) {
        when(template.getEmailTemplate()).thenReturn(CORRECT_TEMPLATE);
        when(template.getTemplateValues(anyString())).thenReturn(templateValues);

        Exception actual = assertThrows(IllegalArgumentException.class,
                () -> templateEngine.generateMessage(incorrectContext));

        assertEquals(actual.getMessage(), EXCEPTION_MESSAGE);
    }


    @DisplayName("generateMessage() returns correct Sting, @ParameterizedTest")
    @ParameterizedTest
    @MethodSource("mapAndStringProvider")
    void generateMessage_whenCorrectContextThenGenerateCorrectMessage_properMessageGenerated(
            HashMap<String, String> correctContext, String resulList) {

        when(template.getEmailTemplate()).thenReturn(CORRECT_TEMPLATE);
        when(template.getTemplateValues(anyString())).thenReturn(templateValues);

        String actual = templateEngine.generateMessage(correctContext);

        assertEquals(actual, resulList);
    }

    static Stream<HashMap<String, String>> mapProvider() {
        return Stream.of(incorrectContextOnlyYearArgument
                , incorrectContextOnlyNameArgument
                , incorrectContextNoActualArguments
                , emptyContext);
    }

    static Stream<Arguments> mapAndStringProvider() {
        return Stream.of(
                arguments(correctContextRealValues, EXPECTED_RESULT_WITH_CORRECT_CONTEXT_VALUES),
                arguments(correctContextDummyValues, EXPECTED_RESULT_WITH_CORRECT_CONTEXT_DUMMY_VALUES),
                arguments(correctContextWithDigits, EXPECTED_RESULT_WITH_DIGITS)
        );
    }

}