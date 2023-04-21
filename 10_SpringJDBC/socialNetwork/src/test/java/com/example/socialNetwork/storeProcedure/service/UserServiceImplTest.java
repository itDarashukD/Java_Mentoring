package com.example.socialNetwork.storeProcedure.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.socialNetwork.TestSupporter;
import com.example.socialNetwork.model.User;
import com.example.socialNetwork.storeProcedure.repository.UserRepositoryImpl;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("tests for UserService class")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest extends TestSupporter {


    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepositoryImpl repository;


    @DisplayName("saveUser(), check if method saveUser() in UserRepository invoked")
    @Test
    void saveUser_dummyUser_saveUserInUserRepositoryInvoked() {
        doNothing().when(repository).saveUser(anyMap());

        service.saveUser(user111);

        verify(repository, times(1)).saveUser(initialDataWithUser111);
    }

    @DisplayName("getAllUsers(), check if method getAllUsers() in UserRepository invoked")
    @Test
    void getAllUsers_emptyList_getAllUsersInUserRepositoryInvoked() {
        when(repository.getAllUsers()).thenReturn(Collections.emptyList());

        service.getAllUsers();

        verify(repository, times(1)).getAllUsers();
    }

    @DisplayName("getBySurname(), check if method getBySurname() in UserRepository invoked")
    @Test
    void getBySurname_dummyUser_getBySurnameInUserRepositoryInvoked() {
        when(repository.getBySurName(anyString())).thenReturn(optionalDummyUser333);

        service.getBySurname(user111.getSurname());

        verify(repository, times(1)).getBySurName(user111.getSurname());
    }

    @DisplayName("updateUser(), check if method updateUser() in UserRepository invoked")
    @Test
    void updateUser_dummyUser_updateUserInUserRepositoryInvoked() {
        when(repository.updateUser(any(User.class))).thenReturn(dummyReturnInt);

        service.updateUser(user111);

        verify(repository, times(1)).updateUser(user111);
    }

    @DisplayName("deleteUserById(), check if method deleteUserById() in UserRepository invoked")
    @Test
    void deleteUserById_dummyUser_deleteUserByIdInUserRepositoryInvoked() {
        when(repository.deleteUserBySurname(anyString())).thenReturn(dummyReturnInt);

        service.deleteUserBySurname(user111.getSurname());

        verify(repository, times(1)).deleteUserBySurname(user111.getSurname());
    }

}