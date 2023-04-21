package com.example.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.ActiveProfiles;

@EnableJms
@ActiveProfiles("integration-test")
@SpringBootTest
class BookingApplicationTests {

	@Test
	void contextLoads() {
	}

}
