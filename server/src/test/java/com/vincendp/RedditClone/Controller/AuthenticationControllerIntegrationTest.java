package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.LoginRequest;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    private UserAuthentication userAuthentication;


    @BeforeEach
    void setup(){
        user = new User();
        user.setCreated_at(new Date());
        user.setUsername("bob");

        userAuthentication = new UserAuthentication();
        userAuthentication.setPassword(passwordEncoder.encode("1234"));
        userAuthentication.setUser(user);
        userAuthenticationRepository.save(userAuthentication);
    }

    @AfterEach
    void cleanup(){
        userAuthenticationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void when_invalid_credentials_throws_error() throws Exception{
        LoginRequest loginRequest = new LoginRequest("alice", "1234");
        String json = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertTrue( result.getResponse().getContentAsString().contains("Error: Invalid username or password") );
    }

    @Test
    void when_authenticate_success_returns_status_200() throws Exception{
        LoginRequest loginRequest = new LoginRequest("bob", "1234");
        String json = objectMapper.writeValueAsString(loginRequest);

        MvcResult  result = mockMvc.perform(post("/auth/login")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertNotNull(result.getResponse().getCookie("jws").getValue());
        assertTrue(result.getResponse().getContentAsString().contains("Success: Logged in"));
    }

}
