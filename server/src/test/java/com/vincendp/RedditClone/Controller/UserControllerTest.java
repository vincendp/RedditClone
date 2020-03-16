package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Service.UserService;
import com.vincendp.RedditClone.Utility.AuthenticationUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationUtility authenticationUtility;


    @Test
    void null_username_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest(null, "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void null_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", null, "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void null_verify_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", null);
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void empty_username_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void empty_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void empty_verify_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void different_passwords_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "1234");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_service_create_user_throws_error() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(IllegalArgumentException.class)
                .thenThrow(NullPointerException.class);

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void user_details_service_load_throws_error() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(new LoginResponse("1", "bob", new Date()));
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenThrow(UsernameNotFoundException.class)
                .thenThrow(new DataAccessException("..."){});

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isInternalServerError());
    }

    // TO DO
    @Test
    void auth_utility_auth_throws_error() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(new LoginResponse("1", "bob", new Date()));
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new CustomUserDetails(new User(), new UserAuthentication()));
//        when(authenticationUtility.authenticateUser(any(UserDetails.class),  any(HttpServletRequest.class) ))

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);



    }









}
