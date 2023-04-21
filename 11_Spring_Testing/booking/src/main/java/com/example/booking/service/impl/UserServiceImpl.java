package com.example.booking.service.impl;

import com.example.booking.dao.UserDao;
import com.example.booking.model.User;
import com.example.booking.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(long userId) {
        final User userById = userDao.getUserById(userId);

        return Optional.ofNullable(userById)
	       .orElseThrow(() -> new IllegalArgumentException(String.format(
		      "user with userId %s did not found",
		      userId)));
    }

    @Override
    public User getUserByEmail(String email) {
        final User userByEmail = userDao.getUserByEmail(email);

        return Optional.ofNullable(userByEmail)
	       .orElseThrow(() -> new IllegalArgumentException(String.format(
		      "User with email %s did not found",
		      email)));
    }

    @Override
    public List<User> getUsersByName(String name) {
        final List<User> usersByName = userDao.getUsersByName(name);

        return usersByName.isEmpty() ? new ArrayList<>() : usersByName;
    }

    @Override
    public User createUser(User user) {
        userDao.createUser(user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        userDao.updateUser(user);

        return user;
    }

    @Override
    public boolean deleteUser(long userId) {
        userDao.deleteUser(userId);

        return true;
    }

}
