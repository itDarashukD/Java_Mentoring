package com.example.booking.dao;

import com.example.booking.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM public.\"User\" WHERE id = #{userId}")
    User getUserById(@Param("userId") long userId);

    @Select("SELECT * FROM public.\"User\" WHERE email = #{email}")
    User getUserByEmail(@Param("email") String email);

    @Result(column = "id", property = "id")
    @Result(column = "name", property = "name")
    @Result(column = "email", property = "email")
    @Select("SELECT * FROM public.\"User\" WHERE name = #{name}")
    List<User> getUsersByName(@Param("name") String name);

    @Insert("INSERT INTO public.\"User\" (name, email) VALUES (#{name}, #{email})")
    void createUser(User user);

    @Update("Update public.\"User\" set name = #{name}, email= #{email} where id=#{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        //give me id of this inserting
    void updateUser(User user);

    @Delete("Delete from public.\"User\" where id=#{userId}")
    void deleteUser(long userId);

}
