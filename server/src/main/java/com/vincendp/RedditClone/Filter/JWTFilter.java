package com.vincendp.RedditClone.Filter;

import com.vincendp.RedditClone.Utility.AuthenticationUtility;
import com.vincendp.RedditClone.Utility.JWTUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JWTFilter extends OncePerRequestFilter {


    private JWTUtility jwtUtility;

    private UserDetailsService userDetailsService;

    private AuthenticationUtility authenticationUtility;

    @Autowired
    public JWTFilter(JWTUtility jwtUtility, UserDetailsService userDetailsService, AuthenticationUtility authenticationUtility){
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
        this.authenticationUtility = authenticationUtility;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        boolean should_delete_cookies = false;

        if(cookies != null && cookies.length > 0){
            cookie = Arrays.asList(cookies).stream().filter(c -> c.getName().equals("jws")).findFirst().orElse(null);
        }

        try{
            if(cookie != null){
                String username = null;
                String jws = cookie.getValue();
                if(jws != null){
                    username = jwtUtility.getUsernameFromClaims(jws);
                    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if(jwtUtility.validateJWS(jws, userDetails)) {
                            authenticationUtility.authenticateUser(userDetails, request);
                        }
                        else{
                            should_delete_cookies = true;
                        }
                    }
                    else{
                        should_delete_cookies = true;
                    }
                }
            }
        }
        catch(SignatureException e){
            should_delete_cookies = true;
            throw new ServletException("Error: Invalid JWT signature");
        }
        catch(ExpiredJwtException e){
            should_delete_cookies = true;
            throw new ServletException("Error: Expired JWT");
        }
        catch(JwtException e){
            should_delete_cookies = true;
            throw new ServletException("Error: Invalid JWT");
        }
        finally {
            if(should_delete_cookies){
                Cookie delete_cookie = new Cookie("jws", null);
                delete_cookie.setMaxAge(0);
                delete_cookie.setPath("/");
                response.addCookie(delete_cookie);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth");
    }
}

