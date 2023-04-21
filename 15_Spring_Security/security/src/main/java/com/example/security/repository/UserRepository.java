package com.example.security.repository;

import com.example.security.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    int addUser(User user);

    int addUserPermission(@Param("name") String name,
                          @Param("email") String email,
                          @Param("permission") String permission);

    User getUserByEmail(@Param("email") String email);

    int saveBlockedUser(User user);

    List<User> getBlockedUsers();
}
