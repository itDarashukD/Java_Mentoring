package com.example.socialNetwork.storeProcedure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.socialNetwork.SocialNetworkApplication;
import com.example.socialNetwork.TestSupporter;
import com.example.socialNetwork.model.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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
@DisplayName("integration tests for interoperability with DB for UserRepository.class")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {SocialNetworkApplication.class})
class UserRepositoryImplTest extends TestSupporter {


    @InjectMocks
    private UserRepositoryImpl repository;
    @Spy
    private JdbcTemplate jdbcTemplate;
    @Spy
    private SimpleJdbcInsert hsqlSimpleJdbcInsert;

    @Autowired
    public UserRepositoryImplTest(@Qualifier("hsqlJdbcTemplate") JdbcTemplate jdbcTemplate,
	   SimpleJdbcInsert hsqlSimpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.hsqlSimpleJdbcInsert = hsqlSimpleJdbcInsert;
    }

    @AfterEach
    public void returnTableToInitialState() {
        deleteFromDb(user111);
        deleteFromDb(user222);
    }

    @DisplayName("saveUser(), check if User was saved to DB")
    @Test
    void saveUser_dummyUser_userSavedToDb() {
        assertFalse(isExist(user111.getSurname()));

        repository.saveUser(initialDataWithUser111);

        Optional<User> userFromDb = getBySurName(user111.getSurname());

        assertEquals(user111, userFromDb.get());
    }

    @DisplayName("getAllUsers(), check if all users were obtained from DB")
    @Test
    void getAllUsers_countOfPrepopulatedUsers_allUserObtainedFromDB() {
        int countOfPrepopulatedUsers = 4;

        List<User> usersFromDb = repository.getAllUsers();

        assertEquals(countOfPrepopulatedUsers, usersFromDb.size());
    }

    @DisplayName("getBySurName(), check if user was obtained form DB")
    @Test
    void getBySurName_dummyUser_userWasObtainedFromDB() {
        saveUser(initialDataWithUser111);

        Optional<User> userFromDb = repository.getBySurName(user111.getSurname());

        assertEquals(user111, userFromDb.get());
    }

    @DisplayName("deleteUserBySurname(), check if user was deleted from DB")
    @Test
    void deleteUserBySurname_dummyUser_userWasDeletedFromDB() {
        saveUser(initialDataWithUser111);
        assertTrue(isExist(user111.getSurname()));

        repository.deleteUserBySurname(user111.getSurname());

        assertFalse(isExist(user111.getSurname()));
    }

    @DisplayName("updateUser(), check if user in DB was updated")
    @Test
    void updateUser_dummyUser_userWasUpdated() {
        Boolean exist = isExist(user222.getSurname());
        assertFalse(exist);

        saveUser(initialDataWithUser222);

        repository.updateUser(user222ToUpdate);

        Optional<User> bySurName = getBySurName(user222.getSurname());

        assertEquals(user222ToUpdate, bySurName.get());
    }

    @DisplayName("dropStoredProcedureAndCreateFewOfThem(), check if a few procedures were removed, and after that were created in DB")
    @Test
    void dropStoredProcedureAndCreateFewOfThem_sqlScriptToDropAndCreate_proceduresWereDroppedAndCreated() throws IOException {
        insertProcedures();

        List<String> storedProceduresInDB = getAllStoredProcedures();
        assertFalse(storedProceduresInDB.isEmpty());

        dropAllStoredProcedures(storedProceduresInDB);

        List<String> allStoredProcedures = getAllStoredProcedures();
        assertTrue(allStoredProcedures.isEmpty());

        insertProcedures();

        List<String> insertedProcedures = getAllStoredProcedures();
        assertFalse(insertedProcedures.isEmpty());
    }

    private void insertProcedures() throws IOException {
        String sqlProcedures = Files.readString(Path.of("./createFewProcedures.sql"));
        jdbcTemplate.execute(sqlProcedures);
    }

    private void dropAllStoredProcedures(List<String> allProcedures) {
        for (String procedure : allProcedures) {
	   jdbcTemplate.update("DROP PROCEDURE " + procedure);
        }
    }

    private List<String> getAllStoredProcedures() {
        return jdbcTemplate.queryForList(GET_ALL_STORED_PROCEDURES, String.class);
    }

    public void saveUser(Map<String, Object> initialUserData) {
        SimpleJdbcInsert insertToUser = hsqlSimpleJdbcInsert.withTableName("public.\"User\"")
	       .usingColumns("name", "surname", "birthdate");

        insertToUser.execute(initialUserData);
    }

    private Boolean isExist(String surname) {
        int count = jdbcTemplate.queryForObject(IS_EXIST_IN_USER_TABLE,
	       new Object[]{surname},
	       Integer.class);

        return count > 0;
    }

    private void deleteFromDb(User user) {
        jdbcTemplate.update(DELETE_FROM_USER, user.getSurname());
    }

    private Optional<User> getBySurName(String surname) {
        return jdbcTemplate.queryForObject(GET_BY_SURE_NAME_FROM_USER,
	       new Object[]{surname},
	       ((rs, rowNum) -> Optional.of(new User(rs.getString("name"),
		      rs.getString("surname"),
		      rs.getDate("birthdate")))));
    }


}

