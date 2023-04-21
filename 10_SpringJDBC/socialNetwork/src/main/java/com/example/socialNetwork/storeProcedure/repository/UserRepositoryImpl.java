package com.example.socialNetwork.storeProcedure.repository;

import com.example.socialNetwork.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String NAME_OF_USERS_WITH_MORE_100_LIKES_AND_MORE_100_FRIENDS = "SELECT u.name,  count(l.user_id) FROM \"Likes\" as l LEFT JOIN \"Friendship\" as f ON (l.user_id = f.user_id1) LEFT JOIN \"User\" as u ON (u.id = l.user_id) where l.timestamp between '2025-03-01 00:00:00.000' and '2025-04-01 00:00:00.000' and f.timestamp between '2025-03-01 00:00:00.000' and '2025-04-01 00:00:00.000' GROUP BY u.name HAVING  count(f.user_id2) >100 ;";
    private static final String SURNAME_LIST_WITH_POST_COUNT_FOR_HALF_OF_YEAR_MORE_1000 = "Select u.surname,  sum(l.post_id) from \"User\" as u LEFT JOIN \"Likes\" as l ON (u.id = l.user_id) where  l.timestamp between '2025-01-01 00:00:00.000' and '2025-06-01 00:00:00.000' GROUP BY u.surname HAVING sum(l.post_id)>1000 ;";
    private static final String SELECT_ALL_FROM_USER = "SELECT * FROM public.\"User\" ";
    private static final String SELECT_ALL_FROM_USER_BY_SURNAME = "SELECT * FROM public.\"User\" WHERE surname = ?";
    private static final String DELETE_FROM_USER = "DELETE FROM public.\"User\" WHERE surname = ?";
    private static final String UPDATE_USER = "UPDATE public.\"User\" SET name = ?, birthdate = ? WHERE surname = ?";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public UserRepositoryImpl(@Qualifier("hikariJdbcTemplate") JdbcTemplate jdbcTemplate,
	   SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public void saveUser(Map<String, Object> initialUserData) {
        SimpleJdbcInsert insertToUser = simpleJdbcInsert.withTableName("public.\"User\"")
	       .usingColumns("name", "surname", "birthdate");

        insertToUser.execute(initialUserData);
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

    @Override
    public Optional<User> getBySurName(String surname) {
        return jdbcTemplate.queryForObject(SELECT_ALL_FROM_USER_BY_SURNAME,
	       new Object[]{surname},
	       ((rs, rowNum) -> Optional.of(new User(rs.getString("name"),
		      rs.getString("surname"),
		      rs.getDate("birthdate")))));
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
    public List<String> getSurnameListWithPostCountForHalfOfYearMore1000() {
        return jdbcTemplate.query(SURNAME_LIST_WITH_POST_COUNT_FOR_HALF_OF_YEAR_MORE_1000,
	       new RowMapper<String>() {
		  @Override
		  public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		      return new String(rs.getString(1));
		  }
	       });
    }


}

