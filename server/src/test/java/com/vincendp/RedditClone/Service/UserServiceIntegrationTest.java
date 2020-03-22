package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private UserRepository userRepository;

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

    @AfterEach
    void cleanup(){
        userAuthenticationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void when_null_password_throws_error(){
        CreateUserRequest createUserRequest = new CreateUserRequest("alice", null, null);
        assertThrows(NullPointerException.class, () -> {
           userService.createUser(createUserRequest);
        });
    }

    @Test
    void when_non_unique_user_throws_error(){
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "1234", "1234");
        assertThrows(DataAccessException.class, () -> {
            userService.createUser(createUserRequest);
        });
    }

    @Test
    void when_valid_signup_returns_login_response(){
        CreateUserRequest createUserRequest = new CreateUserRequest("alice", "1234", "1234");
        LoginResponse loginResponse = userService.createUser(createUserRequest);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getId());
        assertEquals(createUserRequest.getUsername(), loginResponse.getUsername());
    }

}
