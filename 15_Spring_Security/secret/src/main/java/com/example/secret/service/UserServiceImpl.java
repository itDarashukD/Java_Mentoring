package com.example.secret.service;

import com.example.secret.model.User;
import com.example.secret.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final int RETURNED_SUCCESS_RESULT_OF_INSERT_OPERATION = 1;

    @Autowired
    private UserRepository repository;

    @Transactional
    public User addUser(User user) {
        if (isUserExistsInDb(user)) {
            throw new IllegalStateException(String.format("User %s already exist in Db ", user.getName()));
        }

        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));

        int resultOfUserAdding = repository.addUser(user);
        if (resultOfUserAdding != RETURNED_SUCCESS_RESULT_OF_INSERT_OPERATION) {
            throw new IllegalStateException(String.format("User %s was not saved", user.getName()));
        }

        List<Integer> resultOfPermissionsAdding = user.getPermissions().stream().map(permission -> {
            return repository.addUserPermission(user.getName(),
                                                user.getEmail(),
                                                permission.name());
        }).collect(Collectors.toList());

         resultOfPermissionsAdding.forEach(result-> {
             if (result !=1) {
                 throw new IllegalStateException(String.format("Permission for user %s was not saved", user.getName()));
             }
         });
        return user;
    }

    @Override
    public String saveSecretData(String secretText) {
        final String secretDataHash = String.valueOf(secretText.hashCode());

        int resultOfUserAdding =  repository.saveSecretData(secretText, secretDataHash);
        if (resultOfUserAdding != RETURNED_SUCCESS_RESULT_OF_INSERT_OPERATION) {
            throw new IllegalStateException(String.format("Secret data %s was not saved", secretText));
        }
        return secretDataHash;
    }

    @Override
    public String getSecretData(String secretDataHash) {
        String secretData = repository.getSecretDataByHash(secretDataHash);
        if (secretData.isBlank()) {
            throw new IllegalStateException("Secret data was is Blank");
        }
        return secretData;

    }

    @Override
    public void removeSecretData(String secretDataHash) {
        int resultOfUserAdding  = repository.removeSecretDataByHash(secretDataHash);
        if (resultOfUserAdding != RETURNED_SUCCESS_RESULT_OF_INSERT_OPERATION) {
            throw new IllegalStateException(String.format("Secret data %s was not removed", secretDataHash));
        }
    }

    private Boolean isUserExistsInDb(User user) {
        User userByEmail = repository.getUserByEmail(user.getEmail());
        return userByEmail != null;
    }


}
