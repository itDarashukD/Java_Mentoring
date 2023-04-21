package com.example.socialNetwork.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SocialRepositoryImpl implements SocialRepository {

    private final static String NAME_OF_USERS_WITH_MORE_100_LIKES_AND_MORE_100_FRIENDS = "SELECT u.name,  count(l.user_id) FROM \"Likes\" as l LEFT JOIN \"Friendship\" as f ON (l.user_id = f.user_id1) LEFT JOIN \"User\" as u ON (u.id = l.user_id) where l.timestamp between '2025-03-01 00:00:00.000' and '2025-04-01 00:00:00.000' and f.timestamp between '2025-03-01 00:00:00.000' and '2025-04-01 00:00:00.000' GROUP BY u.name HAVING  count(f.user_id2) >100 ; ";
    private final static String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS public.\"\" ( id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),  name text COLLATE pg_catalog.default,  surname text COLLATE pg_catalog.default,  birthdate timestamp without time zone,  CONSTRAINT User_PK PRIMARY KEY (id) )";
    private final static String CREATE_LIKE_TABLE = "CREATE TABLE IF NOT EXISTS public.\"Likes\" (  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ), postId bigint, userId bigint, timestamp timestamp without time zone, CONSTRAINT like_PK PRIMARY KEY (id), CONSTRAINT like_FK FOREIGN KEY (userId) REFERENCES public.User (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID  )";
    private final static String CREATE_POST_TABLE = "CREATE TABLE IF NOT EXISTS public.\"Post\" ( id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),  userId bigint,  text text COLLATE pg_catalog.default,  timestamp timestamp without time zone,  CONSTRAINT post_PK PRIMARY KEY (id) )";
    private final static String CREATE_FRIED_SHIP_TABLE = "CREATE TABLE IF NOT EXISTS public.\"Friendship\" ( id bigint NOT NULL,  userId1 bigint,  userId2 bigint,  timestamp timestamp without time zone,  CONSTRAINT friendship_PK PRIMARY KEY (id) ) ";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public SocialRepositoryImpl(@Qualifier("hikariJdbcTemplate") JdbcTemplate jdbcTemplate,
	   SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public void insertInitialDataToUserTable() {
        SimpleJdbcInsert insertToUser = simpleJdbcInsert.withTableName("public.\"User\"")
	       .usingColumns("name", "surname", "birthdate");

        Map<String, Object> initialUserData = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
	   initialUserData.put("name", "userName" + i);
	   initialUserData.put("surname", "userSurname" + i);
	   initialUserData.put("birthdate", LocalDate.parse("1986-11-11"));

	   insertToUser.execute(initialUserData);
        }
    }

    @Override
    public void insertInitialDataToLikeTable() {
        simpleJdbcInsert.setAccessTableColumnMetaData(true);
        SimpleJdbcInsert insertToLike = simpleJdbcInsert.withTableName("public.\"Likes\"")
	       .usingColumns("post_id", "user_id", "timestamp")
	       .usingGeneratedKeyColumns("id");

        Map<String, Object> initialLikeData = new HashMap<>();
        int y = 1;
        for (int i = 1; i < 300_000; i++) {
	   if (i % 1000 == 0) {
	       y++;
	   }
	   initialLikeData.put("post_id", i);
	   initialLikeData.put("user_id", y);
	   initialLikeData.put("timestamp", getRandomTimestamp());

	   insertToLike.execute(initialLikeData);
        }
    }

    @Override
    public void insertInitialDataToFriendshipTable() {
        SimpleJdbcInsert insertToLike = simpleJdbcInsert.withTableName("public.\"Friendship\"")
	       .usingColumns("user_id1", "user_id2", "timestamp");

        Map<String, Object> initialLikeData = new HashMap<>();

        int userId1 = 1;
        for (int i = 1; i < 70_000; i++) {
	   if (i % 150 == 0) {
	       userId1++;
	   }
	   int userId2 = getRandomInt();
	   if (userId1 == userId2) {
	       continue;
	   }
	   initialLikeData.put("user_id1", userId1);
	   initialLikeData.put("user_id2", userId2);
	   initialLikeData.put("timestamp", getRandomTimestamp());

	   insertToLike.execute(initialLikeData);
        }
    }

    @Override
    public List<String> getNameOfUsersWithMore100LikesAndMore100Friends() {
        return jdbcTemplate.query(NAME_OF_USERS_WITH_MORE_100_LIKES_AND_MORE_100_FRIENDS,
	       new RowMapper<String>() {
		  @Override
		  public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		      return new String(rs.getString(1));
		  }
	       });
    }

    @Override
    public void insertInitialDataToRandomTable(String tableName,
	   List<Map<String, Object>> listMapsToInsert) {
        String[] columNames = listMapsToInsert.stream()
	       .findAny()
	       .get()
	       .keySet()
	       .toArray(new String[0]);

        simpleJdbcInsert.withTableName("public.\"" + tableName + "\"").usingColumns(columNames);

        listMapsToInsert.forEach(map -> simpleJdbcInsert.execute(map));
    }

    public void createStudentTable() {
        jdbcTemplate.execute(CREATE_USER_TABLE);
    }

    public void createLikeTable() {
        jdbcTemplate.execute(CREATE_LIKE_TABLE);
    }

    public void createPostTable() {
        jdbcTemplate.execute(CREATE_POST_TABLE);
    }

    public void createFriedShipTable() {
        jdbcTemplate.execute(CREATE_FRIED_SHIP_TABLE);
    }


    private int getRandomInt() {
        Random rand = new Random();
        return rand.ints(1, 1000).findAny().getAsInt();
    }

    private Timestamp getRandomTimestamp() {
        long offset = Timestamp.valueOf("2025-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2026-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp random = new Timestamp(offset + (long) (Math.random() * diff));

        return random;
    }
}
