package com.example.security.service;

import com.example.security.model.User;
import java.util.List;

public interface UserService {

   User addUser(User user);

   List<User> getBlockedUsers();

}
