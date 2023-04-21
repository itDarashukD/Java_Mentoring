package com.epam.ld.module2.testing.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Tests for Template class")
@ExtendWith(MockitoExtension.class)
class TemplateTest {

    @InjectMocks
    private Template template;

    private static final String EMAIL_TEMPLATE_EMPTY = " ";
    private static final String EMAIL_TEMPLATE_WITHOUT_TEMPLATES = "Dear  Year !";
    private static final String EMAIL_TEMPLATE_INCORRECT_TEMPLATE = "{name} of # New Year !";
    private static final String EMAIL_TEMPLATE_WITH_BOTH_CORRECT_TEMPLATES = " #{name}  #{year} ";
    private static final String EMAIL_TEMPLATE_REAL = "Dear msr. #{name}, Best wishes in terms of #{year} New Year !";
    private static final String EMAIL_TEMPLATE_WITH_DIGITS = "321 #{321},  #{]]]-=_!:@#$%^&&*(} New Year !";
    private static final String EMAIL_TEMPLATE_COLLOCATION_IN_TEMPLATE =
            "Dear msr. #{some collocation},   #{some collocation} New Year !";
    private static final String EMAIL_TEMPLATE_CORRECT_EMPTY_TEMPLATE =
            "Dear msr. #{}, Best wishes in terms of #{} New Year !";
    private static final String EMAIL_TEMPLATE_FEW_TEMPLATES =
            "Dear msr. #{name}, #{name} #{name} #{name} #{year} New Year !";
    private static final String EMAIL_TEMPLATE_PUNCTUATION_IN_TEMPLATE =
            "Dear msr. #{_name_}, #{-name-} #{name} #{name} #{year} New Year !";
    private static final List<String> digits = new ArrayList<>() {{
        add("321");
    }};
    private static final List<String> emptyList = new ArrayList<>();
    private static final List<String> realValues = new ArrayList<>() {{
        add("name");
        add("year");
    }};
    private static final List<String> collocations = new ArrayList<>() {{
        add("some collocation");
        add("some collocation");
    }};
    private static final List<String> toMachRepeatedValues = new ArrayList<>() {{
        add("name");
        add("name");
        add("name");
        add("name");
        add("year");
    }};


    @DisplayName("getTemplateValues() return correct list of values, @TestFactory")
    @TestFactory
    Collection<DynamicTest> getTemplateValues_returnsCorrectValues_worksCorrect() {

        return Arrays.asList(
                dynamicTest("1st dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_EMPTY), emptyList)),
                dynamicTest("2nd dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_REAL), realValues)),
                dynamicTest("3rd dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_WITH_BOTH_CORRECT_TEMPLATES), realValues)),
                dynamicTest("4th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_WITHOUT_TEMPLATES), emptyList)),
                dynamicTest("5th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_COLLOCATION_IN_TEMPLATE), collocations)),
                dynamicTest("6th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_WITH_DIGITS), digits)),
                dynamicTest("7th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_INCORRECT_TEMPLATE), emptyList)),
                dynamicTest("8th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_CORRECT_EMPTY_TEMPLATE), emptyList)),
                dynamicTest("9th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_FEW_TEMPLATES), toMachRepeatedValues)),
                dynamicTest("10th dynamic test", () -> assertEquals(template.getTemplateValues(EMAIL_TEMPLATE_PUNCTUATION_IN_TEMPLATE), toMachRepeatedValues))
        );
    }

}