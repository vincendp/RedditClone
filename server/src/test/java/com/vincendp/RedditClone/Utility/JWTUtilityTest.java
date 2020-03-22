package com.vincendp.RedditClone.Utility;

import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JWTUtilityTest {

    private JWTUtility jwtUtility;
    private String jwt;

    @BeforeEach
    void setup(){
        jwtUtility = new JWTUtility("SECRET", 10000);
        jwt = jwtUtility.generateJWS("bob");
    }

    @Test
    void valid_token_returns_username(){
        assertEquals(jwtUtility.getUsernameFromClaims(jwt), "bob");
    }

    @Test
    void valid_token_returns_expiration(){
        assertTrue(
                Math.abs(jwtUtility.getExpirationFromClaims(jwt).getTime()
                        - new Date(System.currentTimeMillis() + 1000 * 10000).getTime()) < 1000);
    }

    @Test
    void expired_jwt_throws_error(){
        JWTUtility jwtUtility = new JWTUtility("SECRET", 0);
        String jwt = jwtUtility.generateJWS("bob");

        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtility.getExpirationFromClaims(jwt);
        });
    }

    @Test
    void tampered_jwt_throws_error(){
        String tampered_jwt = jwt + "x";

        assertThrows(SignatureException.class, () -> {
           jwtUtility.getUsernameFromClaims(tampered_jwt);
        });
    }

}
