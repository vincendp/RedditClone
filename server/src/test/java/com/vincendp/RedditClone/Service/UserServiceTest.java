package com.vincendp.RedditClone.Service;


import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Exception.ResourceAlreadyExistsException;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Mock
    private UserRepository userRepository;

    private CreateUserRequest createUserRequest;

    private User user;

    @BeforeEach
    void setup(){
        createUserRequest = new CreateUserRequest("bob", "1234", "1234");
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("bob");
        user.setCreated_at(new Date());
    }

    @Test
    void when_user_authentication_repository_fails_throw_error(){
        when(userRepository.findByUsername(anyString())).thenReturn(new User());
        assertThrows(ResourceAlreadyExistsException.class, () -> {
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

        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userAuthenticationRepository.save(any())).thenReturn(userAuthentication);

        assertTrue(userService.createUser(createUserRequest) instanceof LoginResponse);
        assertEquals(createUserRequest.getUsername(), userService.createUser(createUserRequest).getUsername());
    }

    @Test
    void when_invalid_throws_error(){
        when(userRepository.getById(any())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(user.getId().toString());
        });
    }

    @Test
    void when_user_not_found_throws_error(){
        when(userRepository.getById(any())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(user.getId().toString());
        });
    }

    @Test
    void when_valid_returns_login_response(){
        when(userRepository.getById(any())).thenReturn(user);

        assertTrue(userService.getUserById(user.getId().toString()) instanceof LoginResponse);
        assertEquals(createUserRequest.getUsername(),  userService.getUserById(user.getId().toString()).getUsername());
    }

    @Test
    void when_get_user_by_name_and_not_found_should_throw_error(){
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByName("username1");
        });
    }

    @Test
    void when_get_user_by_name_and_found_should_return_dto() throws Exception{
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        LoginResponse loginResponse = userService.getUserByName("bob");
        assertNotNull(loginResponse);
        assertEquals(user.getId().toString(), loginResponse.getId());
        assertEquals(user.getUsername(), loginResponse.getUsername());
        assertEquals(user.getCreated_at(), loginResponse.getCreated_at());
    }
}
