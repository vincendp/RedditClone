package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.LoginRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.JWTUtility;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JWTUtility jwtUtility;
    private UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JWTUtility jwtUtility, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userRepository = userRepository;
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
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customUserDetails.getId());
        String jws = jwtUtility.generateJWS(claims, customUserDetails.getUsername());

        Cookie cookie = new Cookie("jws", jws);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        User user = userRepository.findByUsername(loginRequest.getUsername());

        return ResponseEntity.ok(new SuccessResponse(200,
                "Success: Logged in",
                new LoginResponse(user.getId().toString(), user.getUsername(), user.getCreated_at())));
    }
}

