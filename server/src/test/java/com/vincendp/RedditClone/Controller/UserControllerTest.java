package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.*;
import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Filter.JWTFilter;
import com.vincendp.RedditClone.Service.UserService;
import com.vincendp.RedditClone.Utility.JWTUtility;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtility jwtUtility;

    @Mock
    private UserDetailsService userDetailsService;



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





}
