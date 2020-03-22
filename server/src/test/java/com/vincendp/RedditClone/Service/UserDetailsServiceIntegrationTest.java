package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
@Transactional
public class UserDetailsServiceIntegrationTest {

    @Autowired
    UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private User user;

    private UserAuthentication userAuthentication;

    @BeforeEach
    void setup(){
        user = new User();
        user.setCreated_at(new Date());
        user.setUsername("bob");

        userAuthentication = new UserAuthentication();
        userAuthentication.setPassword("1234");
        userAuthentication.setUser(user);
        userAuthenticationRepository.save(userAuthentication);
    }

    @Test
    void when_username_not_found_throws_error(){
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("alice");
        });
    }

    @Test
    void when_username_found_returns_user_details(){
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername("bob");
        assertNotNull(customUserDetails);
        assertEquals(user.getUsername(), customUserDetails.getUsername());
    }

}
