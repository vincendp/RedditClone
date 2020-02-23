package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    String getUsers(){
        return "hi";
    }

    @PostMapping("/users")
    String createUser(@RequestBody CreateUserRequest createUserRequest){
        if( createUserRequest.getUsername() == null
            || createUserRequest.getPassword() == null
            || createUserRequest.getVerify_password() == null
            || createUserRequest.getUsername().length() <= 0
            || createUserRequest.getPassword().length() <= 0
            || createUserRequest.getVerify_password().length() <= 0){

            throw new IllegalArgumentException("Error: username or password cannot be empty.");
        }
        else if(!createUserRequest.getPassword().equals(createUserRequest.getVerify_password())){
            throw new IllegalArgumentException("Error: passwords are not the same.");
        }

        userService.createUser(createUserRequest);

        return "Created.";
    }

}
