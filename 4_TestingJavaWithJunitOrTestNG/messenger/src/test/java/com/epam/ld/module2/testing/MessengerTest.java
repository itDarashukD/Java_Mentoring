package com.epam.ld.module2.testing;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import com.epam.ld.module2.testing.template.TemplateEngine;

@DisplayName("Tests for Messenger class")
@ExtendWith(MockitoExtension.class)
class MessengerTest {

    @Mock
    private MailServer mailServer;
    @Mock
    private TemplateEngine templateEngine;
    @InjectMocks
    private Messenger messenger = new Messenger(mailServer, templateEngine);

    private static final String MESSAGE = "Dear msr. Dzmitry, Best wishes in terms of 2023 New Year !";
    private static final String EXPECTED_RESULT = "The message to address: ' Bialystok '  ,and content :" +
            " ' Dear msr. Dzmitry, Best wishes in terms of 2023 New Year ! ' ,was sent.";
    private static final HashMap<String, String> incorrectContextAddressAbsent =
            new HashMap<>() {{
                put("year", "2023");
                put("name", "Dzmitry");
            }};
    private static final HashMap<String, String> correctContext =
            new HashMap<>() {{
                put("year", "2023");
                put("name", "Dzmitry");
                put("address", "Bialystok");
            }};


    @Test()
    @DisplayName("prepareMessage() throws IllegalArgumentException when address is blank")
    void prepareMessage_whenAddressIsBlank_exceptionThrow() {
        when(templateEngine.generateMessage(anyMap())).thenReturn(MESSAGE);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> messenger.prepareMessage(incorrectContextAddressAbsent));

        assertEquals(ex.getMessage(), "IN prepareMessage() - address is Blank!)");
    }

    @Test
    @DisplayName("prepareMessage() returns correct message")
    void prepareMessage_returnCorrectMessage_returnedEqualValue() {
        when(templateEngine.generateMessage(anyMap())).thenReturn(MESSAGE);

        String actual = messenger.prepareMessage(correctContext);

        assertEquals(actual, EXPECTED_RESULT);
    }

}
