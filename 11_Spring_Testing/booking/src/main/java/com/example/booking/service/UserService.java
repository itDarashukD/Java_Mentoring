package com.example.booking.service;

import com.example.booking.model.User;
import java.util.List;

public interface UserService {

    User getUserById(long userId);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

}
