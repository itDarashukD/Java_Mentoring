package com.example.security.service;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private UserRepository repository;

    private static final int RETURNED_SUCCESS_RESULT_OF_DAO_OPERATION = 1;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(email)) {
	   saveBlockedUser(email);
	   throw new IllegalStateException(String.format("The user with email : %s was blocked, for 2 minutes", email));
        }
        User userFomDb =  getUserFromDb(email);

        Set<SimpleGrantedAuthority> simpleGrantedAuthority =
	       getSimpleGrantedAuthority(userFomDb);

        org.springframework.security.core.userdetails.User user =
	       new org.springframework.security.core.userdetails.User(userFomDb.getName(),
								userFomDb.getPassword(),
		     						simpleGrantedAuthority);
        return user;
    }

    private User getUserFromDb(String email) {
        User userFomDb = repository.getUserByEmail(email);
        if (userFomDb == null) {
	   throw new UsernameNotFoundException(String.format("The user for email %s not found",
		  email));
        }
        return  userFomDb;
    }

    private void saveBlockedUser(String email) {
        User userFromDb = getUserFromDb(email);

        int insertResult = repository.saveBlockedUser(userFromDb);
        if (insertResult != RETURNED_SUCCESS_RESULT_OF_DAO_OPERATION) {
	   log.error(String.format("The Blocked user with email %s was not saved", email));
        }
    }

    private Set<SimpleGrantedAuthority> getSimpleGrantedAuthority(User userFomDb) {
        Set<SimpleGrantedAuthority> authoritySet = new HashSet<>();
        userFomDb.getPermissions().forEach(permission -> authoritySet.add(new SimpleGrantedAuthority(permission.name())));

        return authoritySet;
    }


}
