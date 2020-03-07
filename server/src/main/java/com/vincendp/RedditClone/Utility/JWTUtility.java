package com.vincendp.RedditClone.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JWTUtility {

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${expiration}")
    private int EXPIRATION;


    public String getUsernameFromClaims(String jws){
        String username;
        try{
            Claims claims = getAllClaimsFromToken(jws);
            username = claims.getSubject();
        }
        catch(Exception e){
            username = null;
        }
        return username;
    }

    public Date getExpirationFromClaims(String jws){
        Date date;
        try{
            Claims claims = getAllClaimsFromToken(jws);
            date = claims.getExpiration();
        }
        catch(Exception e){
            date = null;
        }
        return date;
    }

    private Claims getAllClaimsFromToken(String jws){
        Claims claims;
        try{
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jws).getBody();
        }
        catch(Exception e){
            claims = null;
        }

        return claims;
    }

    public String getJWS(HttpServletRequest request){
        String jws;
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jws = authHeader.substring(7);
        }
        else{
            jws = null;
        }

        return jws;
    }

    public boolean validateJWS(String jws, UserDetails userDetails){
        String username = getUsernameFromClaims(jws);
        return username != null && username.equals(userDetails.getUsername()) &&
                getExpirationFromClaims(jws).before(new Date());

    }

    public String generateJWS(String username){
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

    }


}
