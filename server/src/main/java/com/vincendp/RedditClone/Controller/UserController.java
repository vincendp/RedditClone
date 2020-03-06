package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Model.MyUserDetails;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    String getUsers(){
        MyUserDetails o = userRepository.findUserAndUserAuthentication("vince");
        System.out.println(o.getUsername());
        System.out.println(o.getPassword());
        return "hi";
    }

    @PostMapping("/users")
    String createUser(@RequestBody CreateUserRequest createUserRequest){
        if( createUserRequest.getUsername() == null
            || createUserRequest.getPassword() == null
            || createUserRequest.getVerifyPassword() == null
            || createUserRequest.getUsername().length() <= 0
            || createUserRequest.getPassword().length() <= 0
            || createUserRequest.getVerifyPassword().length() <= 0){

            throw new IllegalArgumentException("Error: username or password cannot be empty.");
        }
        else if(!createUserRequest.getPassword().equals(createUserRequest.getVerifyPassword())){
            throw new IllegalArgumentException("Error: passwords are not the same.");
        }

        System.out.println("hello world");
        System.out.println(createUserRequest.getUsername());
        System.out.println(createUserRequest.getPassword());
        System.out.println(createUserRequest.getVerifyPassword());

        userService.createUser(createUserRequest);

        return "Created.";
    }

}
