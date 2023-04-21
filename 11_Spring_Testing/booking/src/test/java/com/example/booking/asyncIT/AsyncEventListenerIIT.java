package com.example.booking.asyncIT;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.TestSupporter;
import com.example.booking.model.Event;
import com.example.booking.service.impl.EventServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("integration test for AsyncActiveMQ queue")
@AutoConfigureMockMvc
class AsyncEventListenerIIT extends TestSupporter {


    @Autowired
    MockMvc mvc;

    @MockBean
    private EventServiceImpl eventService;

    @DisplayName("asyncCreateEvent() - integration test sent Event to AsyncActiveMQ queue and receive in @Listener method")
    @Test
    void asyncCreateEvent_dummyEvent_asyncListenerMethodWasInvoked() throws Exception {
        when(eventService.createEvent(any(Event.class))).thenReturn(event111);

        mvc.perform(post("/event/asyncCreateEvent").contentType(MediaType.APPLICATION_JSON)
		      .content(objectMapper.writeValueAsString(event111)))
	       .andDo(print())
	       .andExpect(status().isOk());

        verify(eventService, times(1)).createEvent(event111);
    }


}

