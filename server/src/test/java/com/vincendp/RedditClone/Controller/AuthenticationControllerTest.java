package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.LoginRequest;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.JWTUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(AuthenticationController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, AuthenticationController.class })
public class AuthenticationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTUtility jwtUtility;

    @MockBean
    private UserRepository userRepository;

    @Test
    void when_authenticate_fails_then_status_4xx() throws Exception{
        LoginRequest loginRequest = new LoginRequest("bob", "1234");
        String json = objectMapper.writeValueAsString(loginRequest);

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/auth/login")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void when_authenticate_success_returns_status_200() throws Exception{
        User user = new User();
        user.setUsername("bob");
        user.setCreated_at(new Date());
        user.setId(UUID.randomUUID());

        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUser(user);
        userAuthentication.setPassword("1234");
        userAuthentication.setUser_id(user.getId());

        String jws = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
                ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        LoginRequest loginRequest = new LoginRequest(user.getUsername(), userAuthentication.getPassword());
        String json = objectMapper.writeValueAsString(loginRequest);

        CustomUserDetails customUserDetails = new CustomUserDetails(user, userAuthentication);

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(
                customUserDetails, null, null));
        when(jwtUtility.generateJWS(anyString())).thenReturn(jws);
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertEquals(result.getResponse().getCookie("jws").getValue(), jws);
        assertTrue(result.getResponse().getContentAsString().contains("Success: Logged in"));
    }

}
