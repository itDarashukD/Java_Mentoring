package com.example.secret.controller;


import com.example.secret.model.User;
import com.example.secret.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RequestMapping("/secret")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('STANDART')")
    @GetMapping("/about")
    public String getInfo2() {
        return "Hello from ABOUT method";
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PreAuthorize("hasAnyRole('STANDART')")
    @GetMapping("/getSecretForm")
    public ModelAndView getSecretForm() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("secretForm");

        return mav;
    }

    @PostMapping("/saveSecretData")
    public ModelAndView saveSecretData(@RequestParam("secretText") String secretText) {
        String secretDataHash = userService.saveSecretData(secretText);

        String link = prepareOneTimeLink(secretDataHash);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("oneTimeLink");
        mav.addObject("notify","This lik will work only once ! Please take care about your data");
        mav.addObject("oneTimeLink",link);

        return mav;
    }

    @GetMapping(value = "/getSecretData/{secretDataHash}")
    public String getSecretData(@PathVariable("secretDataHash") String secretDataHash) {
        String secretData = userService.getSecretData(secretDataHash);
        userService.removeSecretData(secretDataHash);

        return secretData;
    }

    private String prepareOneTimeLink(String secretDataHash) {
        String serviceUri = ServletUriComponentsBuilder.fromCurrentServletMapping().build().toString();

        return serviceUri + "/secret/getSecretData/" + secretDataHash;
    }

}
