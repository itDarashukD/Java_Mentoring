package com.example.secret.service;

import com.example.secret.model.User;

public interface UserService {

    User addUser(User user);

    String saveSecretData(String secretText);

    String getSecretData(String secretDataHash);

    void removeSecretData(String secretDataHash);

}
