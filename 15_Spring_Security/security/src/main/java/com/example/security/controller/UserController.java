package com.example.security.controller;

import com.example.security.model.User;
import com.example.security.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('VIEW_INFO')")
    @GetMapping("/info")
    public String getInfo() {
        return "Hello from INFO method";
    }

    @GetMapping("/about")
    public String getInfo2() {
        return "Hello from ABOUT method";
    }

    @PreAuthorize("hasAnyRole('VIEW_ADMIN')")
    @GetMapping("/admin")
    public String getAdmin() {
        return "Hello from ADMIN method";
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/getBlockedUsers")
    public List<User> getBlockedUsers() {
        return userService.getBlockedUsers();
    }


}
