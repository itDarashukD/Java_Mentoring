package com.example.event;


import com.example.event.model.Event;
import com.example.event.model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("kafka-test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093",
			     "broker.id=0",
			     "log.dirs=/tmp/kafka-logs",
			     "zookeeper.connect=localhost:2181"},
			      bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {EventApplication.class})
public abstract class AbstractTestSupporter {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String GET_EVENT_BY_ID = "SELECT * FROM public.\"Event\" WHERE event_id = ?";
    private static final String IS_EXIST_IN_EVENT_TABLE = "SELECT count(*) FROM public.\"Event\" WHERE event_id = ?";

    protected final ObjectMapper objectMapper = JsonMapper.builder()
							     .addModule(new JavaTimeModule())
							     .build();
    protected static final Event eventWithId333 = new Event() {{
        setEventId(333);
        setTitle("title333");
        setEventType(EventType.WORKSHOP);
        setSpeaker("speaker333");
        setPlace("place333");
        setDateTime(LocalDateTime.parse("2013-03-13T13:33:33.333333"));
    }};

    protected static final Event eventWithId444 = new Event() {{
        setEventId(444);
        setTitle("title444");
        setEventType(EventType.WORKSHOP);
        setSpeaker("speaker444");
        setPlace("place444");
        setDateTime(LocalDateTime.parse("2014-04-14T14:44:44.444444"));
    }};

    protected static final Event eventWithId555 = new Event() {{
        setEventId(555);
        setTitle("title555");
        setEventType(EventType.WORKSHOP);
        setSpeaker("speaker555");
        setPlace("place555");
        setDateTime(LocalDateTime.parse("2015-05-15T15:55:55.555555"));
    }};

    protected static final Event eventWithId222ToUpdate = new Event() {{
        setEventId(222);
        setTitle("event222ToUpdate");
        setEventType(EventType.WORKSHOP);
        setSpeaker("speaker222ToUpdate");
        setPlace("place222ToUpdate");
        setDateTime(LocalDateTime.parse("2012-02-12T12:22:22.222220"));
    }};


    protected Optional<Event> getEventByEventIdJdbc(long eventId) throws InterruptedException {
        Thread.sleep(1000);
        return jdbcTemplate.queryForObject(GET_EVENT_BY_ID,
	       new Object[]{eventId},
	       ((rs, rowNum) -> Optional.of(new Event(
		      rs.getLong("event_id"),
		      rs.getString("title"),
		      rs.getString("place"),
		      rs.getString("speaker"),
		      EventType.valueOf(rs.getString("event_type")),
		      rs.getTimestamp("date_time").toLocalDateTime()))));
    }

    protected Boolean isExist(long event_id) throws InterruptedException {
        Thread.sleep(1000);
        int count = jdbcTemplate.queryForObject(IS_EXIST_IN_EVENT_TABLE,
	       new Object[]{event_id},
	       Integer.class);

        return count > 0;
    }



}
