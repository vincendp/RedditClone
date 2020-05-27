package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.GetPostPreviewDTO;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import com.vincendp.RedditClone.Utility.ErrorResponse;
import com.vincendp.RedditClone.Utility.JWTUtility;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Test
    void null_username_should_be_caught_by_controller_advice() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest(null, "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        MvcResult result = mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().contains("Error: username or password cannot be empty."));
    }


    @Test
    void non_unique_username_should_be_caught_by_controller_advice() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void when_all_valid_return_ok() throws Exception{
        CreateUserRequest createUserRequest = new CreateUserRequest("bob", "123", "123");
        String json = objectMapper.writeValueAsString(createUserRequest);

        mockMvc.perform(post("/users")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk());

    }

    @Test
    void when_cookie_with_jws_not_valid_should_throw_error(){
        Map<String, Object> claims = new HashMap<>();
        String jws = jwtUtility.generateJWS(claims, "bob").substring(1);


        assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/users")
                    .with(request -> {
                        request.setCookies(new Cookie("jws", jws));
                        return request;
                    })
                    .header("Content-Type", "application/json"));
        });
    }

    @Test
    void when_user_does_not_exist_should_throw_error() throws Exception{
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", UUID.randomUUID().toString());
        String jws = jwtUtility.generateJWS(claims, "bob");

        assertThrows(UsernameNotFoundException.class, ()->{
            mockMvc.perform(get("/users")
                    .with(request -> {
                        request.setCookies(new Cookie("jws", jws));
                        return request;
                    })
                    .header("Content-Type", "application/json"));
        });
    }

    @Test
    void when_valid_jwt_and_user_should_return_success() throws Exception{
        User user = new User();
        user.setUsername("bob");
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPassword("1234");
        userAuthentication.setUser(user);
        userAuthenticationRepository.save(userAuthentication);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userAuthentication.getUser_id().toString());
        String jws = jwtUtility.generateJWS(claims, "bob");

        mockMvc.perform(get("/users")
                .with(request -> {
                    request.setCookies(new Cookie("jws", jws));
                    return request;
                })
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Got user")));
    }

    @Test
    void when_get_user_by_name_and_user_does_not_exist_should_return_error_response() throws Exception{
        MvcResult result = mockMvc.perform(get("/users/{username}", "username1")
                    .header("Content-Type", "application/json"))
                    .andReturn();

        ErrorResponse errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
    }

    @Test
    void when_get_user_by_name_and_found_should_return_success_response() throws Exception{
        User user = new User();
        user.setUsername("bob");
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPassword("1234");
        userAuthentication.setUser(user);
        userAuthenticationRepository.save(userAuthentication);

        MvcResult result = mockMvc.perform(get("/users/{username}", "bob")
                .header("Content-Type", "application/json"))
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        LoginResponse loginResponse = objectMapper.convertValue(successResponse.getResult(), LoginResponse.class);

        assertNotNull(successResponse);
        assertEquals(200, successResponse.getStatus());
        assertNotNull(loginResponse);
        assertEquals(user.getId().toString(), loginResponse.getId());
    }
}
