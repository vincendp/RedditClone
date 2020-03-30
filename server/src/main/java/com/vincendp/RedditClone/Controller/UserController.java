package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Service.UserService;
import com.vincendp.RedditClone.Utility.AuthenticationUtility;
import com.vincendp.RedditClone.Utility.JWTUtility;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
@RequestMapping("users")
public class UserController {

    private UserService userService;

    private UserDetailsService userDetailsService;

    private AuthenticationUtility authenticationUtility;

    private JWTUtility jwtUtility;


    @Autowired
    public UserController(UserService userService,
                          UserDetailsService userDetailsService,
                          AuthenticationUtility authenticationUtility,
                          JWTUtility jwtUtility){
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationUtility = authenticationUtility;
        this.jwtUtility = jwtUtility;
    }

    @GetMapping()
    ResponseEntity getUser(HttpServletRequest request){
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            cookie = Arrays.asList(cookies).stream().filter(c -> c.getName().equals("jws")).findFirst().orElse(null);
        }
        if(cookie == null){
            throw new BadCredentialsException("Error: No cookies found");
        }

        String id = jwtUtility.getIdFromClaims(cookie.getValue());
        LoginResponse loginResponse = userService.getUser(id);

        return ResponseEntity.ok(new SuccessResponse(200, "Success: Got user", loginResponse));
    }

    @PostMapping()
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
