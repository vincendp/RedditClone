package com.vincendp.RedditClone.Service;


import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserAuthenticationRepository userAuthenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setup(){
        createUserRequest = new CreateUserRequest("bob", "1234", "1234");
    }

    @Test
    void when_user_authentication_repository_fails_throw_error(){
        doThrow( new DataIntegrityViolationException("")).when(userAuthenticationRepository).save(any());
        assertThrows(DataAccessException.class, () -> {
           userService.createUser(createUserRequest);
        });
    }


    @Test
    void when_valid_save_returns_login_response(){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("bob");
        user.setCreated_at(new Date());
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUser_id(user.getId());
        userAuthentication.setPassword("1234");
        userAuthentication.setUser(user);
        when(userAuthenticationRepository.save(any())).thenReturn(userAuthentication);

        assertTrue(userService.createUser(createUserRequest) instanceof LoginResponse);
        assertEquals(createUserRequest.getUsername(), userService.createUser(createUserRequest).getUsername());
    }

}
