package com.example.secret.service;

import com.example.secret.model.User;
import com.example.secret.repository.UserRepository;
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
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userFomDb = getUserFromDb(email);

        Set<SimpleGrantedAuthority> simpleGrantedAuthority = getSimpleGrantedAuthority(userFomDb);

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
        return userFomDb;
    }

    private Set<SimpleGrantedAuthority> getSimpleGrantedAuthority(User userFomDb) {
        Set<SimpleGrantedAuthority> authoritySet = new HashSet<>();
        userFomDb.getPermissions()
	       .forEach(permission -> authoritySet.add(new SimpleGrantedAuthority(permission.name())));

        return authoritySet;
    }


}
