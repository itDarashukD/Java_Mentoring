package com.example.socialNetwork.storeProcedure.repository;

import com.example.socialNetwork.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {

    void saveUser(Map<String, Object> initialUserData);

    List<User> getAllUsers();

    Optional<User> getBySurName(String surname);

    int deleteUserBySurname(String surname);

    int updateUser(User user);

    List<String> getNameOfUsersWithMore100LikesAndMore100Friends();

    List<String> getSurnameListWithPostCountForHalfOfYearMore1000();

}
