package com.example.booking;

import com.example.booking.model.Category;
import com.example.booking.model.Event;
import com.example.booking.model.Ticket;
import com.example.booking.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {BookingApplication.class})
public abstract class TestSupporter {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_EVENT_BY_ID = "SELECT * FROM public.\"Event\" WHERE id = ?";
    private static final String GET_TICKET_BY_ID = "SELECT * FROM public.\"Ticket\" WHERE id = ?";

    protected static final Event event111 = new Event() {{
        setId(1);
        setTitle("title111");
        setDate(Date.valueOf("2011-11-11"));
    }};
    protected static final User user111 = new User() {{
        setId(1);
        setName("name111");
        setEmail("email@111.com");
    }};
    protected static final Ticket ticket111 = new Ticket() {{
        setId(1);
        setEventId(1);
        setUserId(1);
        setPlace(1);
        setCategory(Category.BAR);
    }};

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected Optional<Event> getEventByIdJdbc(long id) {
        return jdbcTemplate.queryForObject(GET_EVENT_BY_ID,
	       new Object[]{id},
	       ((rs, rowNum) -> Optional.of(new Event(rs.getLong("id"),
		      rs.getString("title"),
		      rs.getDate("date")))));
    }

    protected Optional<Ticket> getTicketByIdJdbc(long id) {
        return jdbcTemplate.queryForObject(GET_TICKET_BY_ID,
	       new Object[]{id},
	       ((rs, rowNum) -> Optional.of(new Ticket(rs.getLong("id"),
		      rs.getLong("event_id"),
		      rs.getLong("user_id"),
		      rs.getInt("place"),
		      Category.valueOf(rs.getString("category"))))));
    }


}
