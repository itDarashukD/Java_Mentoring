package com.example.security.service;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import com.example.security.util.CustomPasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User addUser(User user) {
        if (isUserExistsInDb(user)) {
            throw new IllegalStateException(String.format("User %s already exist in Db ", user.getName()));
        }

        User userWithEncodedPassword = encodePassword(user);

        int resultOfUserAdding = repository.addUser(userWithEncodedPassword);
        if (resultOfUserAdding != 1) {
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
    public List<User> getBlockedUsers() {
        return repository.getBlockedUsers();
    }

    private Boolean isUserExistsInDb(User user) {
       User userByEmail = repository.getUserByEmail(user.getEmail());
       return userByEmail != null;
    }

    private User encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return user;
    }


}
