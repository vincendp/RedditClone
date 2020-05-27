package com.vincendp.RedditClone.Utility;

import com.vincendp.RedditClone.Model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationUtility {

    private JWTUtility jwtUtility;

    @Autowired
    public AuthenticationUtility(JWTUtility jwtUtility){
        this.jwtUtility = jwtUtility;
    }

    public boolean authenticateUser(UserDetails userDetails, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return true;
    }

    public void setCookie(CustomUserDetails userDetails, HttpServletResponse response){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        String jws = jwtUtility.generateJWS(claims, userDetails.getUsername());
        Cookie cookie = new Cookie("jws", jws);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
