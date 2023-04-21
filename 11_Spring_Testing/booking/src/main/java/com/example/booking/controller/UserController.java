package com.example.booking.controller;


import com.example.booking.model.User;
import com.example.booking.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/user")
@RestController
public class UserController {


    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("getUser/{userid}")
    public User getUserById(@PathVariable long userId) {
        return service.getUserById(userId);
    }

    @GetMapping("getUser/{email}")
    public User getUserById(@PathVariable String email) {
        return service.getUserByEmail(email);
    }

    @GetMapping("getUser/{name} ")
    public List<User> getUsersByName(@PathVariable String name) {
        return service.getUsersByName(name);
    }

    @PostMapping("createUser")
    public User getUsersByName(@RequestBody User user) {
        return service.createUser(user);
    }

    @PutMapping("updateUser")
    public User updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("deleteUser/{userId}")
    public Boolean deleteUser(@PathVariable long userId) {
        return service.deleteUser(userId);
    }

}
