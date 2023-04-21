package com.example.socialNetwork.storeProcedure.service;

import com.example.socialNetwork.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);

    List<User> getAllUsers();

    Optional<User> getBySurname(String surname);

    int updateUser(User user);

    int deleteUserBySurname(String surname);

}
