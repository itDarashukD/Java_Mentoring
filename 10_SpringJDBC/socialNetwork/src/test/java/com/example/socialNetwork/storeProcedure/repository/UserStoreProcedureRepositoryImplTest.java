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
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;


@DisplayName("integration tests for interoperability with DB by saved stored Procedures for UserRepository.class")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {SocialNetworkApplication.class})
class UserStoreProcedureRepositoryImplTest extends TestSupporter {


    @InjectMocks
    private UserStoreProcedureRepositoryImpl repository;
    @Spy
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserStoreProcedureRepositoryImplTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void initialiseTable() throws IOException {
        String sqlProcedures = Files.readString(Path.of("./createFewProcedures.sql"));
        jdbcTemplate.execute(sqlProcedures);
    }

    @AfterEach
    public void returnTableToInitialState() {
        deleteFromDb(user111);
        deleteFromDb(user222);
    }

    @DisplayName("saveUser(), check if User was saved to DB")
    @Test
    void saveUser_dummyUser_userSavedToDb() {
        deleteFromDb(user222);
        assertFalse(isExist(user222.getSurname()));

        repository.saveUser(user222);

        Optional<User> userFromDb = getBySurName(user222.getSurname());

        assertEquals(user222, userFromDb.get());
    }

    @DisplayName("deleteUserBySurname(), check if user was deleted from DB")
    @Test
    void deleteUserBySurname_dummyUser_userWasDeletedFromDB() {
        repository.saveUser(user222);
        assertTrue(isExist(user222.getSurname()));

        repository.deleteUserBySurname(user222.getSurname());

        assertFalse(isExist(user222.getSurname()));
    }

    @DisplayName("updateUser(), check if user in DB was updated")
    @Test
    void updateUser_dummyUser_userWasUpdated() {
        Boolean exist = isExist(user222.getSurname());
        assertFalse(exist);

        repository.saveUser(user222);

        repository.updateUser(user222ToUpdate);

        Optional<User> bySurName = getBySurName(user222.getSurname());

        assertEquals(user222ToUpdate, bySurName.get());
    }


    @DisplayName("getBySurName(), check if user was obtained form DB")
    @Test
    void getBySurName_dummyUser_userWasObtainedFromDB() {
        repository.saveUser(user222);

        Optional<User> userFromDb = repository.getBySurName(user222.getSurname());

        assertEquals(user222, userFromDb.get());
    }

    @DisplayName("getAllUsers(), check if all users were obtained from DB")
    @Test
    void getAllUsers_countOfPrepopulatedUsers_allUserObtainedFromDB() {
        long countOfUsersInDB = getCountOfUsers();

        List<User> usersFromDb = repository.getAllUsers();
        System.out.println(usersFromDb);

        assertEquals(countOfUsersInDB, usersFromDb.size());
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

    private long getCountOfUsers() {
        return jdbcTemplate.queryForObject("SELECT count(id) from public.\"User\"", Long.class);
    }

}