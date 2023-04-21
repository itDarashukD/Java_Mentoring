package com.example.socialNetwork.storeProcedure.repository;

import com.example.socialNetwork.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserStoreProcedureRepositoryImpl implements UserStotreProcedureRepository {

    private static final String SAVE_USER = "call saveUser(?, ?, ?)";
    private static final String SELECT_ALL_FROM_USER = "SELECT * FROM getAllUsers()";
    private static final String SELECT_ALL_FROM_USER_BY_SURNAME = "SELECT * From getBySurname(?)";
    private static final String DELETE_FROM_USER = "call deleteUser(?)";
    private static final String UPDATE_USER = "call updateUser(?,?,?)";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public UserStoreProcedureRepositoryImpl(@Qualifier("hikariJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int saveUser(User user) {
        return jdbcTemplate.update(SAVE_USER,
	       user.getName(),
	       user.getSurname(),
	       user.getBirthDate());
    }

    @Override
    public int deleteUserBySurname(String surname) {
        return jdbcTemplate.update(DELETE_FROM_USER, surname);
    }

    @Override
    public int updateUser(User user) {
        return jdbcTemplate.update(UPDATE_USER,
	       user.getName(),
	       user.getBirthDate(),
	       user.getSurname());
    }

    @Override
    public Optional<User> getBySurName(String surname) {
        return jdbcTemplate.queryForObject(SELECT_ALL_FROM_USER_BY_SURNAME,
	       new Object[]{surname},
	       ((rs, rowNum) -> Optional.of(new User(rs.getString("name"),
		      rs.getString("surname"),
		      rs.getDate("birthdate")))));
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SELECT_ALL_FROM_USER, new RowMapper<User>() {
	   @Override
	   public User mapRow(ResultSet rs, int rowNum) throws SQLException {
	       return new User(rs.getString("name"),
		      rs.getString("surname"),
		      rs.getDate("birthdate"));
	   }
        });
    }


}

