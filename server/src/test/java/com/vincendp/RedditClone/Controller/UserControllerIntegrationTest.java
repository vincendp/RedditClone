package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(status().isBadRequest());
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
}
