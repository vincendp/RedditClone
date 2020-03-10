package com.vincendp.RedditClone.Filter;

import com.vincendp.RedditClone.Utility.JWTUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    @Autowired
    public JWTFilter(JWTUtility jwtUtility, UserDetailsService userDetailsService){
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            cookie = Arrays.asList(cookies).stream().filter(c -> c.getName().equals("jwt")).findFirst().orElse(null);
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
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }

                    }
                }
            }
        }
        catch(SignatureException e){
            throw new ServletException("Error: Invalid JWT signature");
        }
        catch(ExpiredJwtException e){
            throw new ServletException("Error: Expired JWT");
        }
        catch(JwtException e){
            throw new ServletException("Error: Invalid JWT");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth");
    }
}

