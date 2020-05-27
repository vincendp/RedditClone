package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.LoginRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.AuthenticationUtility;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private AuthenticationUtility authenticationUtility;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    AuthenticationUtility authenticationUtility, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authenticationUtility = authenticationUtility;
    }


    @PostMapping(value = "/login")
    ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws Exception{

        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        }
        catch(BadCredentialsException e){
            throw new BadCredentialsException("Error: Invalid username or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        authenticationUtility.setCookie(customUserDetails, response);

        User user = userRepository.findByUsername(loginRequest.getUsername());

        return ResponseEntity.ok(new SuccessResponse(200,
                "Success: Logged in",
                new LoginResponse(user.getId().toString(), user.getUsername(), user.getCreated_at())));
    }
}

