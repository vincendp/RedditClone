package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Service.UserService;
import com.vincendp.RedditClone.Utility.AuthenticationUtility;
import com.vincendp.RedditClone.Utility.JWTUtility;
import io.jsonwebtoken.JwtException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, UserController.class })
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationUtility authenticationUtility;

    @MockBean
    private JWTUtility jwtUtility;

    @Test
    void null_username_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest(null, "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void null_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", null, "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void null_verify_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", null);
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void empty_username_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void empty_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void empty_verify_password_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void different_passwords_should_throw_error() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "1234");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void user_service_create_user_throws_error() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(IllegalArgumentException.class)
                .thenThrow(NullPointerException.class);

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void user_details_service_load_throws_error() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(new LoginResponse("1", "bob", new Date()));
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenThrow(UsernameNotFoundException.class)
                .thenThrow(new DataAccessException(""){});

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is4xxClientError());

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(DataAccessException.class);
    }

    @Test
    void auth_utility_auth_throws_error() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(new LoginResponse("1", "bob", new Date()));
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new CustomUserDetails(new User(), new UserAuthentication()));

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        when(authenticationUtility.authenticateUser(any(UserDetails.class), any(HttpServletRequest.class)))
                .thenThrow(IllegalArgumentException.class)
                .thenThrow(NullPointerException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/users")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void when_all_valid_return_ok() throws Exception{
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(new LoginResponse("1", "bob", new Date()));
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new CustomUserDetails(new User(), new UserAuthentication()));

        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        when(authenticationUtility.authenticateUser(any(UserDetails.class), any(HttpServletRequest.class)))
                .thenReturn(true);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created account")));
    }


    @Test
    void when_cookie_with_jws_not_found_should_throw_error() throws Exception{

        mockMvc.perform(get("/users")
                .with(request -> {
                    request.setCookies(null);
                    return request;
                })
                .header("Content-Type", "application/json"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/users")
                .with(request -> {
                    request.setCookies(new Cookie("hello", "world"));
                    return request;
                })
                .header("Content-Type", "application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_cookie_with_jws_not_valid_should_throw_error(){
        when(jwtUtility.getIdFromClaims(any())).thenThrow(JwtException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(get("/users")
                    .with(request -> {
                        request.setCookies(new Cookie("jws", "jws"));
                        return request;
                    })
                    .header("Content-Type", "application/json"));
        }).hasCauseInstanceOf(JwtException.class);
    }

    @Test
    void when_user_invalid_should_throw_error(){
        when(jwtUtility.getIdFromClaims(any())).thenReturn("id");
        when(userService.getUser(anyString())).thenThrow(ResourceNotFoundException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(get("/users")
                    .with(request -> {
                        request.setCookies(new Cookie("jws", "a"));
                        return request;
                    })
                    .header("Content-Type", "application/json"));
        }).hasCauseInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void when_valid_cookie_and_user_should_return_success() throws Exception{
        when(jwtUtility.getIdFromClaims(any())).thenReturn("id");
        when(userService.getUser(anyString())).thenReturn(new LoginResponse("id", "bob", new Date()));

        mockMvc.perform(get("/users")
                    .with(request -> {
                        request.setCookies(new Cookie("jws", "a"));
                        return request;
                    })
                    .header("Content-Type", "application/json"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(Matchers.containsString("Success: Got user")));

    }
}
