package com.example.socialNetwork.storeProcedure.service;

import com.example.socialNetwork.model.User;
import com.example.socialNetwork.storeProcedure.repository.UserRepositoryImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositoryImpl repository;

    @Override
    public void saveUser(User user) {
        Map<String, Object> initialUserData = new HashMap<>();
        initialUserData.put("name", user.getName());
        initialUserData.put("surname", user.getSurname());
        initialUserData.put("birthdate", user.getBirthDate());

        repository.saveUser(initialUserData);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public Optional<User> getBySurname(String surname) {
        return repository.getBySurName(surname);
    }

    @Override
    public int updateUser(User user) {
        return repository.updateUser(user);
    }

    @Override
    public int deleteUserBySurname(String surname) {
        return repository.deleteUserBySurname(surname);
    }


}
