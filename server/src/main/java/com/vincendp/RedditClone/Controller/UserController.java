package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Service.UserService;
import com.vincendp.RedditClone.Utility.AuthenticationUtility;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private UserService userService;

    private UserDetailsService userDetailsService;

    private AuthenticationUtility authenticationUtility;


    @Autowired
    public UserController(UserService userService,
                          UserDetailsService userDetailsService,
                          AuthenticationUtility authenticationUtility){
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationUtility = authenticationUtility;
    }

    @GetMapping("/helloWorld")
    SuccessResponse getUserss(){
        return new SuccessResponse(200, "", "");
    }

    @PostMapping("/users")
    ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request){
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

        LoginResponse loginResponse = userService.createUser(createUserRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginResponse.getUsername());

        authenticationUtility.authenticateUser(userDetails, request);

        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created account", loginResponse));
    }

}
