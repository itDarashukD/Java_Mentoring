package com.example.booking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.booking.TestSupporter;
import com.example.booking.model.Event;
import com.example.booking.service.EventService;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


@DisplayName("tests for EventServiceImpl.class")
class EventServiceImplTest extends TestSupporter {


    @Autowired
    private EventService eventService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_EVENT_BY_ID = "SELECT * FROM public.\"Event\" WHERE id = ?";
    private static final String IS_EXIST_IN_EVENT_TABLE = "SELECT count(*) FROM public.\"Event\" WHERE id = ?";

    private static final Event event222 = new Event() {{
        setId(2);
        setTitle("title222");
        setDate(Date.valueOf("2022-02-02"));
    }};
    private static final Event event333 = new Event() {{
        setId(3);
        setTitle("title333");
        setDate(Date.valueOf("2003-03-03"));
    }};
    private static final Event event444 = new Event() {{
        setId(4);
        setTitle("title444");
        setDate(Date.valueOf("2004-04-04"));
    }};

    @Test
    void getEventById_dummyEvent_eventObtained() {
        final Event eventById = eventService.getEventById(event111.getId());
        assertEquals(eventById, event111);
    }

    @Test
    void getEventsByTitle_dummyEvent_eventObtained()  {
        final List<Event> eventsByTitle = eventService.getEventsByTitle(event111.getTitle());
        assertTrue(eventsByTitle.contains(event111));
    }

    @Test
    void getEventsForDay_dummyEvent_eventObtained()  {
        final List<Event> eventsForDay = eventService.getEventsForDay(event111.getDate());
        assertTrue(eventsForDay.contains(event111));
    }

    @Test
    void createEvent_dummyEvent_eventCreatedInDb()  {
        final Event eventFromDB = eventService.createEvent(event444);
        assertEquals(eventFromDB, event444);
    }

    @Test
    void updateEvent_dummyEvent_eventUpdatedInDb() {
        Event eventBeforeUpdate = getEventByIdJdbc(event222.getId()).get();

        eventBeforeUpdate.setTitle("updatedTitle555");
        eventBeforeUpdate.setDate(Date.valueOf("2005-05-05"));

        eventService.updateEvent(eventBeforeUpdate);

        Event eventAfterUpdate = getEventByIdJdbc(event222.getId()).get();

        assertEquals(eventAfterUpdate.getTitle(), eventBeforeUpdate.getTitle());
        assertEquals(eventAfterUpdate.getDate(), eventBeforeUpdate.getDate());
    }

    @Test
    void deleteEvent_dummyEvent_eventDeletedFromDb() {
        final Boolean existsBefore = isExist(event333.getId());
        assertTrue(existsBefore);

        final boolean deleted = eventService.deleteEvent(event333.getId());
        assertTrue(deleted);

        final Boolean existsAfter = isExist(event333.getId());
        assertFalse(existsAfter);
    }

    private Boolean isExist(long id) {
        int count = jdbcTemplate.queryForObject(IS_EXIST_IN_EVENT_TABLE,
	       new Object[]{id},
	       Integer.class);

        return count > 0;
    }

    protected Optional<Event> getEventByIdJdbc(long id) {
        return jdbcTemplate.queryForObject(GET_EVENT_BY_ID,
	       new Object[]{id},
	       ((rs, rowNum) -> Optional.of(new Event(rs.getLong("id"),
		      rs.getString("title"),
		      rs.getDate("date")))));
    }


}