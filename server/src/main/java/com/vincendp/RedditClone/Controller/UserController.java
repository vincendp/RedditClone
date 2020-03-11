package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.CreateUserResponse;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Service.UserService;
import com.vincendp.RedditClone.Utility.JWTUtility;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JWTUtility jwtUtility;

    private UserDetailsService userDetailsService;


    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          JWTUtility jwtUtility, UserDetailsService userDetailsService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
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

        CreateUserResponse createUserResponse = userService.createUser(createUserRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(createUserRequest.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created account", createUserResponse));
    }

}
