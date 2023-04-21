package com.example.event;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "kafka-test")
@SpringBootTest
class EventApplicationTests {

    @Test
    void contextLoads() {
    }

}
