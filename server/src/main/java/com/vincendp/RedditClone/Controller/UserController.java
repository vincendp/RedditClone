package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    String getUsers(){
        createUser();
        return "Users.";
    }

    @PostMapping("/users")
    String createUser(){
        User u = new User();
        u.setUsername("Bob");
        userRepository.save(u);

        return "Created.";

    }

}
