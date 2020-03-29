package com.vincendp.RedditClone.Utility;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtility {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${expiration}")
    private int EXPIRATION;

    public JWTUtility(){
    }

    public JWTUtility(String secret, int expiration){
        this.SECRET = secret;
        this.EXPIRATION = expiration;
    }

    public String getUsernameFromClaims(String jws) throws JwtException{
        Claims claims = getAllClaimsFromToken(jws);
        return claims.getSubject();
    }

    public Date getExpirationFromClaims(String jws) throws JwtException{
        Claims claims = getAllClaimsFromToken(jws);
        return claims.getExpiration();
    }

    public String getIdFromClaims(String jws) throws JwtException{
        Claims claims = getAllClaimsFromToken(jws);
        return claims.get("id", String.class);
    }

    private Claims getAllClaimsFromToken(String jws) throws JwtException {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jws).getBody();
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
                getExpirationFromClaims(jws).after(new Date());

    }

    public String generateJWS(Map<String, Object> claims, String username){
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

    }
}
