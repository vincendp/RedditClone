package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserRepository userRepository;
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    public UserController(UserRepository userRepository, UserAuthenticationRepository userAuthenticationRepository){
        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @GetMapping("/users")
    String getUsers(){
        return "Users.";
    }

    @PostMapping("/users")
    String createUser(@RequestBody CreateUserRequest createUserRequest){
        User u = new User();
        UserAuthentication userAuthentication = new UserAuthentication();

        u.setUsername(createUserRequest.getUsername());
        userAuthentication.setPassword(createUserRequest.getPassword());
        userAuthentication.setUser(u);

//        userRepository.save(u);
        userAuthenticationRepository.save(userAuthentication);

        return "Created.";

    }

}
