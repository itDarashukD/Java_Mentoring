package com.example.socialNetwork.storeProcedure.repository;

import com.example.socialNetwork.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStotreProcedureRepository {

    int saveUser(User user);

    int deleteUserBySurname(String surname);

    int updateUser(User user);

    Optional<User> getBySurName(String surname);

    List<User> getAllUsers();

}
