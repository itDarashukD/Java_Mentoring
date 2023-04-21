package com.example.secret.repository;


import com.example.secret.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {

    int addUser(User user);

    int addUserPermission(@Param("name") String name,
                            @Param("email") String email,
                            @Param("permission") String permission);

    User getUserByEmail(@Param("email") String email);

    int saveSecretData(@Param("secret_data")String secret_data,
                       @Param("secret_data_hash")String secret_data_hash);

    String getSecretDataByHash(@Param("secret_data_hash")String secret_data_hash);

    int removeSecretDataByHash(@Param("secret_data_hash")String secret_data_hash);
}
