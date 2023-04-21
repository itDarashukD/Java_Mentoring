package com.example.socialNetwork.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.socialNetwork.SocialNetworkApplication;
import com.example.socialNetwork.TestSupporter;
import com.example.socialNetwork.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("integration-test")
@DisplayName("integration tests for interoperability with DB for SocialRepository.class")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {SocialNetworkApplication.class})
class SocialRepositoryImplTest extends TestSupporter {


    private static final String SELECT_ALL_FROM_USER_BY_SURNAME = "SELECT * FROM public.\"User\" WHERE surname = ?";

    @InjectMocks
    private SocialRepositoryImpl repository;
    @Spy
    private JdbcTemplate jdbcTemplate;
    @Spy
    private SimpleJdbcInsert hsqlSimpleJdbcInsert;


    @Autowired
    public SocialRepositoryImplTest(@Qualifier("hsqlJdbcTemplate") JdbcTemplate jdbcTemplate,
	   SimpleJdbcInsert hsqlSimpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.hsqlSimpleJdbcInsert = hsqlSimpleJdbcInsert;
    }

    @DisplayName("insertInitialDataToUserTable(), check if data for 1000 User were inserted to DB")
    @Test
    void insertInitialDataToUserTable_initialConditions_dataWasIserted() {
        Boolean isLastUserExistBeforeInsert = isExist(999L, "User");
        assertFalse(isLastUserExistBeforeInsert);

        repository.insertInitialDataToUserTable();

        Boolean isLastUserExistAfterInsert = isExist(999L, "User");
        assertTrue(isLastUserExistAfterInsert);
    }

    @DisplayName("insertInitialDataToLikeTable(), check if data for 300_000 Likes were inserted to DB")
    @Test
    void insertInitialDataToLikeTable_initialConditions_dataWasInserted() {
        Boolean isLastLikeExistBeforeInsert = isExist(299_999L, "Likes");
        assertFalse(isLastLikeExistBeforeInsert);

        repository.insertInitialDataToLikeTable();

        Boolean isLastLikeExistAfterInsert = isExist(299_999L, "Likes");
        assertTrue(isLastLikeExistAfterInsert);
    }

    @DisplayName("insertInitialDataToFriendshipTable(), check if data for 60_000 Friendships were inserted to DB")
    @Test
    void insertInitialDataToFriendshipTable_initialConditions_dataWasInserted() {
        Boolean isLastLikeExistBeforeInsert = isExist(59_999L, "Friendship");
        assertFalse(isLastLikeExistBeforeInsert);

        repository.insertInitialDataToFriendshipTable();

        Boolean isLastLikeExistAfterInsert = isExist(59_999L, "Friendship");
        assertTrue(isLastLikeExistAfterInsert);
    }

    @DisplayName("getNamesOfUsersWithMore100LikesAndMore100Friends(), check if names were obtained from DB")
    @Test
    void getNamesOfUsersWithMore100LikesAndMore100Friends_initialConditions_namesWereObtained() {
        repository.insertInitialDataToUserTable();
        repository.insertInitialDataToLikeTable();
        repository.insertInitialDataToFriendshipTable();

        List<String> userNames = repository.getNameOfUsersWithMore100LikesAndMore100Friends();

        assertFalse(userNames.isEmpty());
    }

    @DisplayName("insertInitialDataToRandomTable(), check if data for 2 Users were inserted to DB User table")
    @Test
    void insertInitialDataToRandomTable_listOdDataToInsert_dataWasInserted() {
        String tableName = "User";
        List<Map<String, Object>> listMapsToInsert = new ArrayList<>() {{
	   add(initialDataWithUser111);
	   add(initialDataWithUser222);
        }};

        Boolean isLastLikeExistBeforeInsert = isExist(5L, "User");
        assertFalse(isLastLikeExistBeforeInsert);

        repository.insertInitialDataToRandomTable(tableName, listMapsToInsert);

        Boolean isLastLikeExistAfterInsert = isExist(5L, "User");
        assertTrue(isLastLikeExistAfterInsert);

        final Optional<User> userFromDb111 = getBySurName(user111.getSurname());
        final Optional<User> userFromDb222 = getBySurName(user222.getSurname());

        assertEquals(userFromDb111.get(), user111);
        assertEquals(userFromDb222.get(), user222);
    }

    private Boolean isExist(Long id, String tableName) {
        String sql = "SELECT count(*) FROM public.\"" + tableName + "\" WHERE id = ?";

        int count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);

        return count > 0;
    }

    private Optional<User> getBySurName(String surname) {
        return jdbcTemplate.queryForObject(SELECT_ALL_FROM_USER_BY_SURNAME,
	       new Object[]{surname},
	       ((rs, rowNum) -> Optional.of(new User(rs.getString("name"),
		      rs.getString("surname"),
		      rs.getDate("birthdate")))));
    }


}